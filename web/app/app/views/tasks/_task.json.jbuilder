json.extract! task, :id, :name, :group, :scan_type, :created_at, :updated_at
json.url task_url(task, format: :json)