module AppConstants
	module ScanTypes
		STATIC = "Static".freeze
		MOBILE = "Mobile".freeze
		WEB = "Web".freeze
		NETWORK = "Network".freeze
		WORDPRESS = "Wordpress".freeze
		HARDCODE = "SecretToken".freeze
		UPLOADED = "Uploaded"
	end
	module OwnerTypes
		CORPORATE = 'corporate'.freeze
		TEAM = "team".freeze
		PERSONAL = "personal".freeze
	end
	module UserMode
		SINGLE_USER = "SingleUser".freeze
	end
	module SeverityTypes
		CRITICAL = "Critical".freeze
		HIGH = "High".freeze
		MEDIUM = "Medium".freeze
		LOW = "Low".freeze
		INFO = "Info".freeze
	end
	module ScanStatus
		QUEUED = "Queued".freeze
		SCANNING = "Scanning".freeze
		COMPLETED = "Completed".freeze
		PENDING = "Pending".freeze
	end
	module FindingStatus
		NEW = "New".freeze
		OPEN = "Open".freeze
		CLOSED = "Close".freeze
		FIX_IN_PROGRESS = "Fix in progress".freeze
		DEFERRED = "Deferred".freeze
		STATUS_TYPES = [["New", "New"],["Open", "Open"],["Close", "Close"],["Fix in progress", "Fix in progress"],['Deferred','Deferred']]
	end
	module SourceTypes
		SOURCE_TYPES = [["Gitlab", "gitlab"],["Github", "github"],["Local", "local"]]
	end
	module ScheduledTypes
		PERIODIC_TYPES = [["Daily", "Daily"],["Weekly", "Weekly"],["Monthly", "Monthly"]] 
	end
	module NmapParameters
		NMAP_OPTIONS = [["Connect Scan", "T"],["Syn Scan", "S"],["Ack Scan", "A"],["Window Scan", "W"],["Maimon Scan", "M"],["Null Scan", "N"],["Fin Scan", "F"],["Xmas Scan", "X"],["UDP Scan", "U"]]
	end
	module TOOLS
		EXTERNAL_TOOLS = [["Acunetix", "Acunetix"],["Nmap", "Nmap"],["Burp", "Burp"],["Zap", "Zap"],["Nessus", "Nessus"],["Qualys", "Qualys"],["OpenVAS","OpenVAS"],["Metasploit","Metasploit"],["Nexpose","Nexpose"],["Arachni","Arachni"],["IBMApp","IBMApp"],["Fortify","Fortify"],["SkipFish","SkipFish"],["W3af","W3af"]]
		DEFAULT_EXTERNAL_TOOLS = ["Acunetix","Burp","Zap","Nessus","Qualys","OpenVAS","Metasploit","Nexpose","IBMApp","Fortify","SkipFish","W3af"]
		ALL_TOOLS = [["Acunetix", "Acunetix"],["Nmap", "Nmap"],["Burp", "Burp"],["Zap", "Zap"],["Nessus", "Nessus"],["Qualys", "Qualys"],["OpenVAS","OpenVAS"],["Metasploit","Metasploit"],["Nexpose","Nexpose"],["Arachni","Arachni"],["IBMApp","IBMApp"],["Fortify","Fortify"],["SkipFish","SkipFish"],["W3af","W3af"],["PMD","PMD"],["FindSecurityBugs","FindSecurityBugs"],["Xanitizer","Xanitizer"],["NPM","NPM"],["NodeSecurityProject","NodeSecurityProject"],["RetireJS","RetireJS"],["Brakeman","Brakeman"],["BundleAudit","BundleAudit"],["Dawnscanner","Dawnscanner"]]

	end
	module AppTypes
		APP_TYPES = ["Static","Web","Wordpress","Mobile","Network"]
		APP_TYPES_WITH_VALUES = [["Source Code","Static"],["Web","Web"],["Wordpress","Wordpress"],["Mobile","Mobile"],["Network","Network"]]
	end

end 
