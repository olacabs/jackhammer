require 'pipeline/mounters/base_mounter'

class Pipeline::IPMounter < Pipeline::BaseMounter
	Pipeline::Mounters.add self

	def initialize trigger, options
		super(trigger)
		@options = options
		@name = "IP"
		@description = "Mount a IP - typically for a live attack."
	end

	def mount target
		return target
	end

	def supports? target
		ip_parts = target.split('.')
		return false if ip_parts.length != 4
		ip_parts.each do |it|
			return false if it.length==0 || !(it =~ /\A\d+\z/ ? true : false) || it.to_i<0 || it.to_i>255
		end
		return true
	end
end
