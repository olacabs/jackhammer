# Tracks internal pipeline events.
# Can be used for control, but also tracking what happens.
class Pipeline::Event
  attr_reader :parent
  attr_accessor :path
  attr_accessor :appname
  attr_accessor :scan_id

  def initialize appname, parent = nil
  	@appname = appname
   	@parent = parent
   	@timestamp = Time.now
  end

end
