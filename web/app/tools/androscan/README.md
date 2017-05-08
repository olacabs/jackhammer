#AndroGuard

JackHammer Usage

`python mandrobugs.py -t {jk scan id} -e 2 -o {tmp output dir} -j {tmp report file} -f {absolute apk path}`

Example 

`python mandrobugs.py -t scanid -e 2 -o scanid -j scanid.json -f /Users/home/Downloads/test.apk  `
<pre>
usage: mandrobugs.py [-h] [-j JSON_FILE] -f APK_FILE [-m ANALYZE_MODE]
                     [-b ANALYZE_ENGINE_BUILD] [-t ANALYZE_TAG] [-e EXTRA]
                     [-c LINE_MAX_OUTPUT_CHARACTERS] [-s] [-v]
                     [-o REPORT_OUTPUT_DIR]

AndroBugs Framework - Android App Security Vulnerability Scanner

optional arguments:
  -h, --help            show this help message and exit
  -j JSON_FILE, --json_file JSON_FILE
                        Json file to store results
  -f APK_FILE, --apk_file APK_FILE
                        APK File to analyze
  -m ANALYZE_MODE, --analyze_mode ANALYZE_MODE
                        Specify "single"(default) or "massive"
  -b ANALYZE_ENGINE_BUILD, --analyze_engine_build ANALYZE_ENGINE_BUILD
                        Analysis build number.
  -t ANALYZE_TAG, --analyze_tag ANALYZE_TAG
                        Analysis tag to uniquely distinguish this time of
                        analysis.
  -e EXTRA, --extra EXTRA
                        1)Do not check(default) 2)Check security class names,
                        method names and native methods
  -c LINE_MAX_OUTPUT_CHARACTERS, --line_max_output_characters LINE_MAX_OUTPUT_CHARACTERS
                        Setup the maximum characters of analysis output in a
                        line
  -s, --store_analysis_result_in_db
                        Specify this argument if you want to store the
                        analysis result in MongoDB. Please add this argument
                        if you have MongoDB connection.
  -v, --show_vector_id  Specify this argument if you want to see the Vector ID
                        for each vector.
  -o REPORT_OUTPUT_DIR, --report_output_dir REPORT_OUTPUT_DIR
                        Analysis Report Output Directory
</pre>
