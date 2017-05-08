"""
Added json support to AndroBugs.
For future versions of AndroBugs, Apply patch from libs/AndroBugs/patch.diff

"""
import sys, os
from datetime import datetime
import json
import traceback

try:
    pwd = os.path.dirname(os.path.realpath(__file__))
    sys.path.append(pwd + "/libs/AndroBugs/")
    sys.path.append(pwd + "/libs/AndroBugs/tools")
    from androbugs import Writer, __analyze, parseArgument, parser
except:
    raise Exception("AndroBugs not found in lib path.")

parser.add_argument("-j", "--json_file", type=str, help="Json file to store results", default=str(datetime.now())+".json")

args = parseArgument()
packed_analyzed_results = {}
search_list = set()


def json_serial(obj):
    """Serialize datetime object to ISO format"""
    if isinstance(obj, datetime):
        serial = obj.isoformat()
        return serial
    raise TypeError ("Type not serializable")


with open("strings.txt") as fp:
    to_match = fp.read().splitlines()
    search_list.update([x.strip() for x in to_match])

try:
    """
        - Analyse the apk
        - Extract  strings from APK and match with user supplied list
    """
    writer = Writer()
    extracted_strings, _, _ = __analyze(writer, args)
    stripped = set([x.strip() for x in extracted_strings])
    sensitive_data = search_list.intersection(stripped)
    packed_analyzed_results = writer.get_packed_analyzed_results_for_mongodb()
    packed_analyzed_results["sensitive"] = list(sensitive_data)
except Exception, e:
    print traceback.print_exc()


result_file = args.json_file
with open(result_file, "w") as fp:
    json.dump(packed_analyzed_results, fp, default=json_serial)
