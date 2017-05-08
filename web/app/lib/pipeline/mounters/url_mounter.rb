require 'pipeline/mounters/base_mounter'

class Pipeline::URLMounter < Pipeline::BaseMounter
  Pipeline::Mounters.add self
  
  def initialize trigger, options
    super(trigger)
    @options = options
    @name = "URL"
    @description = "Mount a url - typically for a live attack."
  end

  def mount target
    return target
  end

  def supports? target
    start = target.slice(0,4)
    last = target.slice(-4,target.length)
    if last === ".git"
      return false
    elsif start === "http"
      return true
    else
      return false
    end
  end
end
