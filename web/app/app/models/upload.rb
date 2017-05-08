class Upload < ActiveRecord::Base
	belongs_to :finding
	belongs_to :user
	has_attached_file :file
        validates_attachment :file, content_type: { content_type: [/\Aimage\/.*\Z/,'application/pdf'] } 
        do_not_validate_attachment_file_type :file	
end
