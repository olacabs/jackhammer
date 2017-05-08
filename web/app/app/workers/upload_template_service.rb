module UploadTemplateService
	def process_template(args={})
		@template = args.fetch(:template)
		@plugin   = args.fetch(:plugin)
		@data     = "#{@plugin.capitalize}::ReportItem".constantize.new(args.fetch(:data))
		template_source.gsub( /%(.*?)%/ ) do |field|
			name = field[1..-2]
			if fields.include?(name)
				value(field: name)
			else
				"Field [#{field}] not recognized by the plugin"
			end
		end	
	end
	def fields
		@fields ||= {}
		@fields[@template] ||= begin
					       fields_file = File.join(default_templates_dir, "#{@template}.fields")
					       File.readlines(fields_file).map(&:chomp)
				       end
	end
	def value(args={})
		field = args[:field]
		# fields in the template are of the form <foo>.<field>, where <foo>
		# is common across all fields for a given template (and meaningless).
		_, name = field.split('.')
		@data.try(name) || 'n/a'
	end
	def template_source
		@sources = {}
		# The template can change from one time to the next (via the Plugin Manager)
		template_file  = File.join(default_templates_dir, "#{@template}.template")
		template_mtime = File.mtime(template_file)

		@sources[@template] = {
			mtime: template_mtime,
			content: File.read(template_file)
		}
		@sources[@template][:content]
	end
	def default_templates_dir
		File.join(Rails.root, "templates/plugins/", @plugin.to_s)
	end
	def save_finding(args={})
		fingerprint = Digest::MD5.hexdigest("#{@scan.target}#{args[:bug_type]}#{args[:severity]}#{args[:detail]}#{args[:references]}#{args[:code]}#{args[:line]}#{args[:file]}#{args[:port]}#{args[:scanner]}#{args[:confidence]}#{args[:host]}#{args[:protocol]}#{args[:scripts]}#{args[:cvss_score]}#{args[:cvss_score]}#{args[:location]}#{args[:request]}#{args[:response]}")
		if @scan.team_id.present?
			previous = Finding.by_repo_id(@scan.repo_id).by_teams(@scan.team_id).fingerprint(fingerprint).order("created_at").last
		else
			previous = Finding.by_repo_id(@scan.repo_id).by_user(@scan.user_id).fingerprint(fingerprint).order("created_at").last
		end
		if previous.present?
			unless @scan.findings.include?(previous)
				@scan.findings << previous
				@scan.scaner_instances.last.findings << previous
			end
			previous.scaners << @scan unless previous.scaners.include?(@scan)
		else
			begin
				f = Finding.create({
					description: args[:bug_type],
					severity: args[:severity],
					scanner: args[:scanner],
					cvss_score: args[:cvss_score],
					detail: args[:detail],
					external_link: args[:references],
					port: args[:port],
					protocol: args[:protocol],
					product: args[:product],
					scripts: args[:scripts],
					version: args[:version],
					host: args[:host],
					state: args[:state],
					solution: args[:solution],
					location: args[:location],
					file: args[:file],
					request: args[:request],
					response: args[:response],
					confidence: args[:confidence],
					repo_id: @scan.repo_id,
					user_id: @scan.user_id,
					scan_type: @scan.scan_type,
					owner_type: @scan.owner_type,
					team_id: @scan.team_id,
					scaner_instance_id: @scan.scaner_instances.last.id,
					code: args[:code],
					line: args[:line],
					scaner_id: @scan.id,
					fingerprint: fingerprint
				})

				if f.valid?
					f.scaners << @scan unless f.scaners.include?(@scan)
					@scan.findings << f unless @scan.findings.include?(f)
				end
			rescue Exception=>e
			end
		end
	end	
	def update_sev_count
		sev_hash = {"Critical"=>0,"High"=>0,"Medium"=>0,"Low"=>0,"Info"=>0}
		findings = @scan.findings.group_by_severity
		findings.each do |sev_counts|
			sev_hash[sev_counts[0]] = sev_counts[1]
		end
		@total_count = sev_hash["Critical"] + sev_hash["High"] + sev_hash["Medium"] + sev_hash["Low"] + sev_hash["Info"]
		@scan.update(critical_count: sev_hash["Critical"] ,high_count: sev_hash["High"] ,medium_count: sev_hash["Medium"] ,low_count: sev_hash["Low"],info_count: sev_hash["Info"],total_count: @total_count,status: 'Completed' ,status_reason: 'Results are uploaded')
		@scan.end_time = Time.now.asctime
		@scan.save
		#@scan.scaner_instances.last.update(critical_count: @critical_count,high_count:  @high_count ,medium_count:  @medium_count ,low_count:  @low_count,info_count: @info_count,total_count: @total_count )
		#	if @scan.team.present?
		#		@scan.repo.update(total_count: @scan.repo.total_count  + @total_count, critical_count: @scan.repo.critical_count + @critical_count,high_count: @scan.repo.high_count + @high_count,medium_count: @scan.repo.medium_count + @medium_count,low_count: @scan.repo.low_count + @low_count,info_count: @scan.repo.info_count + @info_count)
		#		@scan.team.update(total_count: @scan.team.total_count + @total_count , critical_count: @scan.team.critical_count + @critical_count,high_count: @scan.team.high_count + @high_count,medium_count: @scan.team.medium_count + @medium_count,low_count: @scan.team.low_count + @low_count,info_count: @scan.team.info_count + @info_count)
		#	end
	end
end
