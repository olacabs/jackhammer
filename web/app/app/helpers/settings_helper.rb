module SettingsHelper
	def is_tool_enabled?(task_name)
		return true if Setting.pipeline.present? && Setting.pipeline["enabled_tools"].include?(task_name)
		return false
	end
end
