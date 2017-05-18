require 'pipeline/tasks/base_task'
require 'pipeline/util'
require 'nokogiri'

class Pipeline::NikitoScanner < Pipeline::BaseTask
        Pipeline::Tasks.add self
        include Pipeline::Util
        def initialize(trigger, tracker)
                super(trigger, tracker)
                @name = 'NiktoScanner'
                @description = "Nikto website scanner"
                @stage = :code
                @labels << "code" << "ruby" << "rails"
        end

        def run
                Dir.chdir '/home/app/tools/nikto/program'
                @results_file = Tempfile.new(['nikitoresult', '.xml'])
                runsystem(true,'perl', 'nikto.pl', '-host', "#{@trigger.path}", '-Format', 'xml', '-o', "#{@results_file.path}")
                @result = File.open("#{@results_file.path}"){ |f| Nokogiri::XML(f) }
        end

        def analyze
        begin
                @result.xpath('//item').each do |item|
                        description = item.xpath('description').text
                        uri = item.xpath('uri').text
                        namelink = item.xpath('namelink').text
                        bug_type = 'website issue'
                        severity = "High"
                        location = ""
                        line = ""
                        code = ""
                        user_input = ""
                        advisory = ""
                        solution = ""
                        cvss_score = ""
                        file = ""
                        source = {:scanner => @name, :file => file, :line => line, :code => code}
                        fprint = fingerprint("#{description}#{uri}#{namelink}")
                        report bug_type,description,source,severity,fprint,line,location,user_input, advisory,solution,cvss_score
                end
        rescue Exception =>e
                        Pipeline.warn e.message
                        Pipeline.warn e.backtrace
        end
        ensure
                FileUtils.rm @results_file
        end

        def supported?
                cmd = `/home/app/tools/nikto/program/nikto.pl -V`
                return cmd.to_s.include?("Nikto") ? true: false
        end
end
