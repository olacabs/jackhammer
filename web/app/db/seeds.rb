# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
#  static tools
static_scan_type = ScanType.create(name: 'Static Tools')
#ruby tools
task = Task.create([{name: 'Brakeman',group: 'Ruby',gem_name: 'brakeman' ,scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: true},{name: 'BundleAudit',group: 'Ruby',gem_name:'bundle-audit',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: true},{name: 'Dawnscanner',group: 'Ruby',gem_name: 'dawnscanner',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: true}])
#java tools
task = Task.create([{name: 'NodeSecurityProject',group: 'Javascript',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: true},{name: 'RetireJS',group: 'Javascript',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: true}])
task = Task.create([{name: 'PMD',group: 'Java',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: false},{name: 'FindSecurityBugs',group: 'Java',scan_type_id: static_scan_type.id,can_upgrade: true,is_direct_upgrade: false},{name: 'Xanitizer',group: 'Java',scan_type_id:static_scan_type.id,can_upgrade: true,is_direct_upgrade: false}])
#dynamic tools
web_scan_type = ScanType.create(name: 'Web Tools')
#web tools
task = Task.create(name: 'Arachni',scan_type_id: web_scan_type.id,gem_name: 'arachni,arachni-reactor',can_upgrade: true,is_direct_upgrade: true)
#
wrodpress_scan_type = ScanType.create(name: 'Wordpress Tools')
#wordpress tools
task = Task.create(name: 'Wpscan',scan_type_id: wrodpress_scan_type.id,is_custom: true,can_upgrade: false,is_direct_upgrade: false)
mobile_scan_type = ScanType.create(name: 'Mobile App Tools') 
#mobile tools
task = Task.create(name: 'AndroScanner',scan_type_id: mobile_scan_type.id,is_custom: true,can_upgrade: false,is_direct_upgrade: false)
network_scan_type = ScanType.create(name: 'Network Tools')
#network tools
task = Task.create(name: 'Nmap',scan_type_id: network_scan_type.id,can_upgrade: true,is_direct_upgrade: true)
#hardcode secrets
hardcode_secrets_scan_type = ScanType.create(name: 'Hardcoded Secret Tools') 
task = Task.create(name: 'TruffleScanner',scan_type_id: hardcode_secrets_scan_type.id,is_custom: true,can_upgrade: false,is_direct_upgrade: false)
#severity levels
task = Task.create([{name: 'critical',group: 'Severity'},{name: 'high',group: 'Severity'},{name: 'medium',group: 'Severity'},{name: 'low',group: 'Severity'},{name: 'info',group: 'Severity'}])
#tasks
task = Task.create([{name: 'Add Scan',group: 'AssignedTask'},{name: 'Delete Scan',group: 'AssignedTask'},{name: 'Mark False Positive',group: 'AssignedTask'},{name: 'Tagging',group: 'AssignedTask'},{name: 'Upload Files',group: 'AssignedTask'}])
#roles
roles =  Role.create([{name: 'QA'},{name: 'Dev'}])
#roles =  Role.create([{name: 'Team Lead'},{name: 'Admin'}])
#teams
default_teams = Team.create([{name: 'Wordpress',is_default_team: true},{name: 'Network',is_default_team: true},{name: 'Web',is_default_team: true},{name: 'Mobile',is_default_team: true}])
#admin user
if ENV['APPLICATION_MODE'] != "SingleUser"
	admin_user = User.create(name: 'jackhammer',email: "jackhammer@olacabs.com",password: "j4ckh4mm3r", is_admin?: true)
end
functionality = Functionality.create([{name: "Scan"},{name: "Upload Files"},{name: "Mark False Positive"},{name: "Comments"},{name: "Change Vulnerable Status"},{name: 'Tagging'}])
roles.each do |role|
	role.functionalities << Functionality.all
	role.update(function_operations: "scan_read,scan_create,scan_delete,upload_read,upload_delete")
end
Setting.application_mode = ENV['APPLICATION_MODE']
