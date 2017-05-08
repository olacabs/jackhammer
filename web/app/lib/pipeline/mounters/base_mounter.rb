  
class Pipeline::BaseMounter
  attr_reader :errors
  attr_reader :trigger
  attr_accessor :name
  attr_accessor :description

  def initialize(trigger)
    @errors = []
    @trigger = trigger
  end

  def error error
    @errors << error
  end

  def name
    @name
  end

  def description
    @description
  end

  def mount
  end

  def supports? target
  end

end
