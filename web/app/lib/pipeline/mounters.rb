# Knows how to test file types and then defer to the helper.

require 'pipeline/event'

class Pipeline::Mounters
  @mounters = []

  attr_reader :target
  attr_reader :warnings
   
  def self.add klass
    @mounters << klass unless @mounters.include? klass
  end

  def self.mounters
  	@mounters
  end

  def initialize
  	@warnings = []
  end

  def add_warning warning
    @warnings << warning
  end

  def self.mount tracker
  	target = tracker.options[:target]
  	Pipeline.debug "Mounting target: #{target}"
  	trigger = Pipeline::Event.new(tracker.options[:appname])
  	@mounters.each do | c |
  	  mounter = c.new trigger, tracker.options
 	  begin 
	  	Pipeline.debug "Checking about mounting #{target} with #{mounter}"
  	    if mounter.supports? target
	  	  Pipeline.notify "Mounting #{target} with #{mounter}"
	  	  path = mounter.mount target
	  	  Pipeline.notify "Mounted #{target} with #{mounter}"
		  return path
	  	end
	  rescue => e 
	  	Pipeline.notify e.message
	  end
  	end
  end

   def self.get_mounter_name mounter_class
    mounter_class.to_s.split("::").last
  end
end

#Load all files in mounters/ directory
Dir.glob("#{File.expand_path(File.dirname(__FILE__))}/mounters/*.rb").sort.each do |f|
  require f.match(/(pipeline\/mounters\/.*)\.rb$/)[0]
end