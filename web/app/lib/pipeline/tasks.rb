require 'thread'

#Collects up results from running different tasks.
#
#tasks can be added with +task.add(task_class)+
#
#All .rb files in tasks/ will be loaded.
class Pipeline::Tasks
  @tasks = []
  @optional_tasks = []

  attr_reader :tasks_run

  #Add a task. This will call +_klass_.new+ when running tests
  def self.add klass
    @tasks << klass unless @tasks.include? klass
  end

  #Add an optional task
  def self.add_optional klass
    @optional_tasks << klass unless @tasks.include? klass
  end

  def self.tasks
    @tasks + @optional_tasks
  end

  def self.optional_tasks
    @optional_tasks
  end

  def self.initialize_tasks task_directory = ""
    #Load all files in task_directory
    Dir.glob(File.join(task_directory, "*.rb")).sort.each do |f|
      require f
    end
  end

  #No need to use this directly.
  def initialize options = { }
    @warnings = []
    @tasks_run = []
  end

  #Add Warning to list of warnings to report.
  def add_warning warning
    @warnings << warning
  end

  #Run all the tasks on the given Tracker.
  #Returns a new instance of tasks with the results.
  def self.run_tasks(target, stage, tracker)
    task_runner = self.new
    trigger = Pipeline::Event.new(tracker.options[:appname])
    trigger.path = target
    trigger.scan_id = tracker.options[:scan_id]
    self.tasks_to_run(tracker).each do |c|
      task_name = get_task_name c

      #Run or don't run task based on options
      #Now case-insensitive specifiers:  nodesecurityproject = Pipeline::NodeSecurityProject

      if tracker.options[:skip_tasks]
        skip_tasks = tracker.options[:skip_tasks].map {|task| task.downcase}
      end
      if (tracker.options[:run_tasks])
        run_tasks = tracker.options[:run_tasks].map {|task| task.downcase}
      end

      unless skip_tasks.include? task_name.downcase or
        (run_tasks and not run_tasks.include? task_name.downcase)

        task = c.new(trigger, tracker)
        begin
          if task.supported? and task.stage == stage
            if task.labels.intersect? tracker.options[:labels] or          # Only run tasks with labels
                 ( run_tasks and run_tasks.include? task_name.downcase )   # or that are explicitly requested.
              Pipeline.notify "#{stage} - #{task_name} - #{task.labels}"
              task.run
              task.analyze
              task.findings.each do | finding |
                tracker.report finding
              end
           end
          end
        rescue => e
          Pipeline.notify e.message
          tracker.error e
        end

        task.warnings.each do |w|
          task_runner.add_warning w
        end

        #Maintain list of which tasks were run
        #mainly for reporting purposes
        task_runner.tasks_run << task_name[5..-1]
      end
    end

    task_runner
  end


  private

  def self.get_task_name task_class
    task_class.to_s.split("::").last
  end

  def self.tasks_to_run tracker
    if tracker.options[:run_all_tasks] or tracker.options[:run_tasks]
      @tasks + @optional_tasks
    else
      @tasks
    end
  end
end

#Load all files in tasks/ directory
Dir.glob("#{File.expand_path(File.dirname(__FILE__))}/tasks/*.rb").sort.each do |f|
  require f.match(/(pipeline\/tasks\/.*)\.rb$/)[0]
end
