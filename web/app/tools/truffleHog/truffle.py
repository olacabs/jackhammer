#!/usr/bin/env python
import sys
import math
import datetime
import argparse
import json
import re
import tempfile
from git import Repo, Git

if sys.version_info[0] == 2:
    reload(sys)
    sys.setdefaultencoding('utf8')

BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
HEX_CHARS = "1234567890abcdefABCDEF"

DEFAULT_BRANCH = "master"

def shannon_entropy(data, iterator):
    """
    Borrowed from http://blog.dkbza.org/2007/05/scanning-data-for-entropy-anomalies.html
    """
    if not data:
        return 0
    entropy = 0
    for x in (ord(c) for c in iterator):
        p_x = float(data.count(chr(x)))/len(data)
        if p_x > 0:
            entropy += - p_x*math.log(p_x, 2)
    return entropy


def get_strings_of_set(word, char_set, threshold=20):
    count = 0
    letters = ""
    strings = []
    for char in word:
        if char in char_set:
            letters += char
            count += 1
        else:
            if count > threshold:
                strings.append(letters)
            letters = ""
            count = 0
    if count > threshold:
        strings.append(letters)
    return strings

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def find_strings(project_path,outfile, branch, depth, from_date, to_date, regex):
    """
    Read the repo. Check the active branch.
    If branch args is not specified scan the repo as it is.
    Else If active branch !== branch_args; then checkout branch_args.
    If depth is not specified; Initialize scan_depth= MAX_DEPTH.
    Get the latest N(scan_depth) commits.
    Create a diff between n and n-1 commits.
    For each line in diff:
        do regex mtching
        For each word in line:
            do entropy test
        If any results; Save to outfile.

    :param project_path: source directory
    :param outfile: json result file
    :return: project path
    """
    start_date = None
    end_date = None
    if from_date:
        start_date = datetime.datetime.strptime(from_date, "%d-%m-%Y")
    if to_date:
        end_date = datetime.datetime.strptime(to_date, "%d-%m-%Y")

    fp = open(outfile, "a")
    fp.write("[\n")

    repo = Repo(project_path)

    already_searched = set()

    if not branch:
        # scan all branches
        for remote_branch in repo.remotes.origin.fetch():
            branch_name = str(remote_branch).split('/')[1]
            try:
                repo.git.checkout(remote_branch)
            except Exception, e:
                print e.message
            # print repo, depth, branch_name, already_searched, regex
            extract_sensitive(repo=repo, depth=depth, branch_name=branch_name, already_searched=already_searched,
                              fp=fp, from_date=start_date, to_date=end_date, regex=regex)

    else:
        # checkout a specific branch
        branch_name = repo.active_branch.name
        if branch != branch_name:
            for remote_branch in repo.remotes.origin.fetch():
                name = str(remote_branch).split('/')[1]
                try:
                    if name == branch_name:
                        repo.git.checkout(remote_branch, b=branch_name)
                except Exception, e:
                    print e.message

        extract_sensitive(repo=repo, depth=depth, branch_name=branch_name, already_searched=already_searched, fp=fp,
                          from_date=start_date, to_date=end_date, regex=regex)

    fp.write("]\n")
    fp.close()
    return project_path


def extract_sensitive(repo, branch_name, fp, already_searched, depth=None, from_date=None, to_date=None, regex=None):

    first_object_written = False
    prev_commit = None

    if not depth:
        commits = repo.iter_commits()
    else:
        commits = repo.iter_commits(max_count=depth)

    for curr_commit in commits:
        commit_date = curr_commit.committed_datetime.replace(tzinfo=None)
        if from_date and commit_date < from_date:
            continue
        if to_date and commit_date > to_date:
            continue

        # print "Scanning", str(curr_commit), curr_commit.committed_datetime

        if not prev_commit:
            pass
        else:
            # avoid searching the same diffs
            hashes = str(prev_commit) + str(curr_commit)
            if hashes in already_searched:
                prev_commit = curr_commit
                continue
            already_searched.add(hashes)

            diff = prev_commit.diff(curr_commit, create_patch=True)
            for blob in diff:
                #  print i.a_blob.data_stream.read()
                my_diff = []
                try:
                    printableDiff = blob.diff.decode()
                except:
                    try:
                        printableDiff = str(blob.diff) # error in decoding blob
                    except:
                        print "error in decoding diff ", str(curr_commit)
                        continue  # unable to convert diff to string

                if printableDiff.startswith("Binary files"):
                    continue

                foundSomething = False

                lines = printableDiff.split("\n")
                for line in lines:
                    if regex:
                        for pattern in regex:
                            matched = re.search(pattern, line, re.MULTILINE | re.IGNORECASE)
                            if matched:
                                foundSomething = True
                                display_vulnerable(curr_commit,matched.group(), prev_commit, branch_name)
                                my_diff.append([{"vulnerable": matched.group(), "extras": line, "matched": "regex"}])

                    for word in line.split():
                        base64_strings = get_strings_of_set(word, BASE64_CHARS)
                        hex_strings = get_strings_of_set(word, HEX_CHARS)
                        for string in base64_strings:
                            b64Entropy = shannon_entropy(string, BASE64_CHARS)
                            if b64Entropy > 4.5:
                                foundSomething = True
                                my_diff.append([{"vulnerable": string, "matched": "base64entropy"}])
                                # printableDiff = printableDiff.replace(string, bcolors.WARNING + string + bcolors.ENDC)
                                display_vulnerable(curr_commit,string, prev_commit, branch_name)
                        for string in hex_strings:
                            hexEntropy = shannon_entropy(string, HEX_CHARS)
                            if hexEntropy > 3:
                                foundSomething = True
                                my_diff.append([{"vulnerable": string, "matched": "hexentropy"}])
                                # printableDiff = printableDiff.replace(string, bcolors.WARNING + string + bcolors.ENDC)
                                display_vulnerable(curr_commit,string, prev_commit, branch_name)
                if foundSomething:
                    commit_time =  datetime.datetime.fromtimestamp(prev_commit.committed_date).strftime('%Y-%m-%d %H:%M:%S')
                    temp = {"date": commit_time, "branch":branch_name, "commit":  prev_commit.message.strip(), "diff": my_diff}

                    if not first_object_written:
                        fp.write(json.dumps(temp))
                        first_object_written = True
                    else:
                        fp.write(",\n")
                        fp.write(json.dumps(temp))

                        # print(printableDiff)
        prev_commit = curr_commit

    pass


def display_vulnerable(curr_commit,vulnerable, prev_commit, branch_name):
    commit_time =  datetime.datetime.fromtimestamp(prev_commit.committed_date).strftime('%Y-%m-%d %H:%M:%S')
    print(bcolors.OKGREEN + "CommitId: " + str(curr_commit) + bcolors.ENDC)
    print(bcolors.OKGREEN + "Vulnerable: " + vulnerable  + bcolors.ENDC)
    print(bcolors.OKGREEN + "Date: " + commit_time + bcolors.ENDC)
    print(bcolors.OKGREEN + "Branch: " + branch_name + bcolors.ENDC)
    print(bcolors.OKGREEN + "Commit: " + prev_commit.message + bcolors.ENDC)


def prepare_repo(parser, git_url, path=None, branch=DEFAULT_BRANCH, depth=None):
    """Setup the repo"""

    repo_path = None

    if git_url:
        if not path:
            repo_path = tempfile.mkdtemp()
        else:
            repo_path = path
        if depth:
            Git().clone(git_url, repo_path, depth=depth)
        else:
            Git().clone(git_url, repo_path)
    else:
        if path:
            repo_path = path
        else:
            parser.print_help()
            exit(-1)
    return repo_path

def parse_regex(rfile):
    """Read file and return regex"""
    if rfile:
        with open(rfile) as fp:
            lines = fp.readlines()
            lines = map(lambda x: x.strip(), lines)
            return lines
    return None

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='Find secrets hidden in the depths of git.')
    parser.add_argument('--git',  type=str, help='Git url for cloning the repo', default=None)
    parser.add_argument('--path',  type=str, help='Local repo path', default=None)
    parser.add_argument('--outfile', type=str, help='Filename for output', default=str(datetime.datetime.now()) + '.json')
    parser.add_argument('--branch', type=str, help='Scan branch', default=None)
    parser.add_argument('--depth', type=int, help='Scan depth', default=None)
    parser.add_argument('--start', type=str, help='Scan from date', default=None)
    parser.add_argument('--end', type=str, help='Scan to date', default=None)
    parser.add_argument('--regex', type=str, help='regex file')

    args = parser.parse_args()
    project_path = prepare_repo(parser, args.git, args.path, args.branch, args.depth)
    regex = parse_regex(args.regex)
    find_strings(project_path=project_path, outfile=args.outfile, branch=args.branch, depth=args.depth,  from_date=args.start, to_date=args.end, regex=regex)
