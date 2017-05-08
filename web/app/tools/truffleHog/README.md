# Truffle Hog
Searches through git repositories for high entropy strings, digging deep into commit history and branches. This is effective at finding secrets accidentally committed that contain high entropy.

```
usage: truffle.py [-h] [--git GIT] [--path PATH] [--outfile OUTFILE]
                  [--branch BRANCH] [--depth DEPTH] [--start START]
                  [--end END]

Find secrets hidden in the depths of git.

optional arguments:
  -h, --help         show this help message and exit
  --git GIT          Git url for cloning the repo
  --path PATH        Local repo path
  --outfile OUTFILE  Filename for output
  --branch BRANCH    Scan branch
  --depth DEPTH      Scan depth
  --start START      Scan from date dd-mm-YYYY
  --end END          Scan to date dd-mm-YYYY
  --regex REGEX      regex file path
```
Requirements
 `pip install -I GitPython==2.1.1`
