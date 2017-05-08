require 'pipeline/mounters/base_mounter'

class Pipeline::DockerMounter < Pipeline::BaseMounter

  Pipeline::Mounters.add self
  
  #Pass in path to the root of the Rails application
  def initialize trigger, options
  	super(trigger)
    @options = options
  end

  def mount target
    base = @options[:working_dir]
    target = target.slice(0, target.length - 7)
    working_target = base + "/docker/" + target + "/"
    Pipeline.notify "Cleaning directory: #{working_target}"
    if ! working_target.match(/\A.*\/line\/tmp\/.*/)
      Pipeline.notify "Bailing in case #{working_target} is malicious."      
    else
      result = system("rm" ,"-rf", "#{working_target}")
      Pipeline.debug result
      result = system("mkdir", "-p", "#{working_target}")
      Pipeline.debug result
      result = system("docker","export" ,"#{target}", ">" ,"#{working_target}#{target}.tar")
      Pipeline.debug result
      result = system("tar" ,"-C","#{working_target}", "-xf", "#{working_target}#{target}.tar")
      Pipeline.debug result
      result = system("rm", "#{working_target}#{target}.tar")
      Pipeline.debug result
    end
    return working_target
  end
  
  def supports? target
    last = target.slice(-7,target.length)
    Pipeline.debug "In Docker mounter, target: #{target} became: #{last} ... wondering if it matched .docker"
    if last === ".docker"
      return true
    else
      return false
    end
  end
end
