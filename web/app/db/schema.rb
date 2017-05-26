# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20170524061211) do

  create_table "alert_notifications", force: :cascade do |t|
    t.integer  "user_id",    limit: 4
    t.string   "identifier", limit: 255
    t.string   "alert_type", limit: 255
    t.boolean  "read",                   default: false
    t.string   "message",    limit: 255
    t.datetime "created_at",                             null: false
    t.datetime "updated_at",                             null: false
    t.integer  "scaner_id",  limit: 4
    t.string   "task_name",  limit: 255
  end

  add_index "alert_notifications", ["user_id"], name: "index_alert_notifications_on_user_id", using: :btree

  create_table "branches", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.integer  "repo_id",    limit: 4
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "comments", force: :cascade do |t|
    t.integer  "finding_id", limit: 4
    t.string   "message",    limit: 255
    t.integer  "tag_id",     limit: 4
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
    t.integer  "user_id",    limit: 4
  end

  create_table "configurations", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.string   "value",      limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "configurations", ["name"], name: "index_configurations_on_name", unique: true, using: :btree

  create_table "filters", force: :cascade do |t|
    t.string   "filter_name",   limit: 255
    t.string   "filter_values", limit: 255
    t.integer  "user_id",       limit: 4
    t.datetime "created_at",                null: false
    t.datetime "updated_at",                null: false
  end

  create_table "findings", force: :cascade do |t|
    t.integer  "repo_id",            limit: 4
    t.integer  "branch_id",          limit: 4
    t.boolean  "current"
    t.text     "description",        limit: 65535
    t.string   "severity",           limit: 255
    t.string   "fingerprint",        limit: 255
    t.string   "first_appeared",     limit: 255
    t.text     "detail",             limit: 65535
    t.string   "scanner",            limit: 255
    t.text     "file",               limit: 65535
    t.string   "line",               limit: 255
    t.text     "code",               limit: 65535
    t.datetime "created_at",                                       null: false
    t.datetime "updated_at",                                       null: false
    t.integer  "scaner_id",          limit: 4
    t.string   "status",             limit: 255,   default: "New"
    t.boolean  "is_false_positive",                default: false
    t.text     "external_link",      limit: 65535
    t.text     "solution",           limit: 65535
    t.string   "cvss_score",         limit: 255
    t.string   "location",           limit: 255
    t.string   "user_input",         limit: 255
    t.string   "advisory",           limit: 255
    t.integer  "scaner_instance_id", limit: 4
    t.string   "closed_by",          limit: 255
    t.string   "issue_type",         limit: 255
    t.date     "closed_date"
    t.string   "port",               limit: 255
    t.string   "protocol",           limit: 255
    t.string   "state",              limit: 255
    t.string   "product",            limit: 255
    t.text     "scripts",            limit: 65535
    t.string   "version",            limit: 255
    t.string   "host",               limit: 255
    t.text     "request",            limit: 65535
    t.text     "response",           limit: 65535
    t.string   "confidence",         limit: 255
    t.boolean  "not_exploitable",                  default: false
    t.boolean  "is_publish_to_jira",               default: false
    t.string   "port_status",        limit: 255
    t.string   "host_status",        limit: 255
    t.string   "scan_type",          limit: 255
    t.integer  "team_id",            limit: 4
    t.integer  "user_id",            limit: 4
    t.integer  "tag_id",             limit: 4
    t.string   "owner_type",         limit: 255
    t.string   "jira_key",           limit: 255
  end

  add_index "findings", ["created_at"], name: "index_findings_on_created_at", using: :btree
  add_index "findings", ["is_false_positive"], name: "index_findings_on_is_false_positive", using: :btree
  add_index "findings", ["owner_type"], name: "index_findings_on_owner_type", using: :btree
  add_index "findings", ["repo_id"], name: "index_findings_on_repo_id", using: :btree
  add_index "findings", ["scan_type"], name: "index_findings_on_scan_type", using: :btree
  add_index "findings", ["scaner_id"], name: "index_findings_on_scaner_id", using: :btree
  add_index "findings", ["scaner_instance_id"], name: "index_findings_on_scaner_instance_id", using: :btree
  add_index "findings", ["scanner"], name: "index_findings_on_scanner", using: :btree
  add_index "findings", ["severity"], name: "index_findings_on_severity", using: :btree
  add_index "findings", ["tag_id"], name: "index_findings_on_tag_id", using: :btree
  add_index "findings", ["team_id"], name: "index_findings_on_team_id", using: :btree
  add_index "findings", ["user_id"], name: "index_findings_on_user_id", using: :btree

  create_table "findings_scaners", id: false, force: :cascade do |t|
    t.integer "finding_id", limit: 4, null: false
    t.integer "scaner_id",  limit: 4, null: false
  end

  add_index "findings_scaners", ["finding_id", "scaner_id"], name: "index_findings_scaners_on_finding_id_and_scaner_id", using: :btree
  add_index "findings_scaners", ["scaner_id", "finding_id"], name: "index_findings_scaners_on_scaner_id_and_finding_id", using: :btree

  create_table "findings_tags", id: false, force: :cascade do |t|
    t.integer "finding_id", limit: 4, null: false
    t.integer "tag_id",     limit: 4, null: false
  end

  add_index "findings_tags", ["finding_id", "tag_id"], name: "index_findings_tags_on_finding_id_and_tag_id", using: :btree
  add_index "findings_tags", ["tag_id", "finding_id"], name: "index_findings_tags_on_tag_id_and_finding_id", using: :btree

  create_table "functionalities", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.boolean  "is_active?",             default: true
    t.datetime "created_at",                            null: false
    t.datetime "updated_at",                            null: false
  end

  create_table "functionalities_permissions", id: false, force: :cascade do |t|
    t.integer "functionality_id", limit: 4, null: false
    t.integer "permission_id",    limit: 4, null: false
  end

  create_table "functionalities_roles", id: false, force: :cascade do |t|
    t.integer "functionality_id", limit: 4, null: false
    t.integer "role_id",          limit: 4, null: false
  end

  create_table "notifications", force: :cascade do |t|
    t.integer  "user_id",        limit: 4
    t.integer  "critical_count", limit: 4
    t.integer  "high_count",     limit: 4
    t.integer  "medium_count",   limit: 4
    t.integer  "low_count",      limit: 4
    t.string   "critical_email", limit: 255
    t.string   "medium_email",   limit: 255
    t.string   "low_email",      limit: 255
    t.string   "info_email",     limit: 255
    t.integer  "info_count",     limit: 4
    t.string   "high_email",     limit: 255
    t.datetime "created_at",                 null: false
    t.datetime "updated_at",                 null: false
  end

  create_table "permissions", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.string   "status",     limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "permissions_roles", id: false, force: :cascade do |t|
    t.integer "role_id",       limit: 4, null: false
    t.integer "permission_id", limit: 4, null: false
  end

  create_table "repos", force: :cascade do |t|
    t.string   "name",           limit: 255
    t.string   "full_name",      limit: 255
    t.integer  "service_portal", limit: 1
    t.string   "ssh_repo_url",   limit: 255
    t.string   "languages",      limit: 255
    t.integer  "forked",         limit: 1
    t.datetime "created_at",                             null: false
    t.datetime "updated_at",                             null: false
    t.integer  "team_id",        limit: 4
    t.integer  "total_count",    limit: 4,   default: 0
    t.integer  "critical_count", limit: 4,   default: 0
    t.integer  "high_count",     limit: 4,   default: 0
    t.integer  "medium_count",   limit: 4,   default: 0
    t.integer  "low_count",      limit: 4,   default: 0
    t.integer  "info_count",     limit: 4,   default: 0
    t.string   "git_type",       limit: 255
    t.string   "repo_type",      limit: 255
  end

  add_index "repos", ["critical_count"], name: "index_repos_on_critical_count", using: :btree
  add_index "repos", ["high_count"], name: "index_repos_on_high_count", using: :btree
  add_index "repos", ["info_count"], name: "index_repos_on_info_count", using: :btree
  add_index "repos", ["low_count"], name: "index_repos_on_low_count", using: :btree
  add_index "repos", ["medium_count"], name: "index_repos_on_medium_count", using: :btree
  add_index "repos", ["team_id"], name: "index_repos_on_team_id", using: :btree
  add_index "repos", ["total_count"], name: "index_repos_on_total_count", using: :btree

  create_table "repos_users", id: false, force: :cascade do |t|
    t.integer "repo_id", limit: 4, null: false
    t.integer "user_id", limit: 4, null: false
  end

  create_table "roles", force: :cascade do |t|
    t.string   "name",                limit: 255
    t.integer  "resource_id",         limit: 4
    t.string   "resource_type",       limit: 255
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "function_operations", limit: 255
  end

  add_index "roles", ["name", "resource_type", "resource_id"], name: "index_roles_on_name_and_resource_type_and_resource_id", using: :btree
  add_index "roles", ["name"], name: "index_roles_on_name", using: :btree

  create_table "roles_teams", id: false, force: :cascade do |t|
    t.integer "team_id", limit: 4
    t.integer "role_id", limit: 4
  end

  add_index "roles_teams", ["team_id", "role_id"], name: "index_roles_teams_on_team_id_and_role_id", using: :btree

  create_table "roles_users", id: false, force: :cascade do |t|
    t.integer "role_id", limit: 4, null: false
    t.integer "user_id", limit: 4, null: false
  end

  create_table "scan_types", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "scaner_instances", force: :cascade do |t|
    t.integer  "scaner_id",      limit: 4
    t.datetime "created_at",                           null: false
    t.datetime "updated_at",                           null: false
    t.integer  "total_count",    limit: 4, default: 0
    t.integer  "critical_count", limit: 4, default: 0
    t.integer  "high_count",     limit: 4, default: 0
    t.integer  "medium_count",   limit: 4, default: 0
    t.integer  "low_count",      limit: 4, default: 0
    t.integer  "info_count",     limit: 4, default: 0
  end

  add_index "scaner_instances", ["critical_count"], name: "index_scaner_instances_on_critical_count", using: :btree
  add_index "scaner_instances", ["high_count"], name: "index_scaner_instances_on_high_count", using: :btree
  add_index "scaner_instances", ["info_count"], name: "index_scaner_instances_on_info_count", using: :btree
  add_index "scaner_instances", ["low_count"], name: "index_scaner_instances_on_low_count", using: :btree
  add_index "scaner_instances", ["medium_count"], name: "index_scaner_instances_on_medium_count", using: :btree
  add_index "scaner_instances", ["scaner_id"], name: "index_scaner_instances_on_scaner_id", using: :btree
  add_index "scaner_instances", ["total_count"], name: "index_scaner_instances_on_total_count", using: :btree

  create_table "scaners", force: :cascade do |t|
    t.integer  "user_id",                     limit: 4
    t.string   "target",                      limit: 255
    t.string   "project_title",               limit: 255
    t.datetime "created_at",                                                null: false
    t.datetime "updated_at",                                                null: false
    t.integer  "repo_id",                     limit: 4
    t.string   "branch_name",                 limit: 255
    t.string   "status",                      limit: 255
    t.string   "status_reason",               limit: 255
    t.string   "source",                      limit: 255
    t.string   "project_target_file_name",    limit: 255
    t.string   "project_target_content_type", limit: 255
    t.integer  "project_target_file_size",    limit: 4
    t.datetime "project_target_updated_at"
    t.integer  "branch_id",                   limit: 4
    t.integer  "no_of_processed_files",       limit: 4
    t.integer  "directories",                 limit: 4
    t.string   "start_time",                  limit: 255
    t.string   "end_time",                    limit: 255
    t.string   "found_langs",                 limit: 255
    t.string   "invloved_tools",              limit: 255
    t.integer  "team_id",                     limit: 4
    t.string   "periodic_schedule",           limit: 255
    t.date     "last_run"
    t.boolean  "is_scanned",                                default: false
    t.string   "scan_type",                   limit: 255
    t.string   "vulnerable_types",            limit: 255
    t.integer  "total_count",                 limit: 4,     default: 0
    t.integer  "critical_count",              limit: 4,     default: 0
    t.integer  "high_count",                  limit: 4,     default: 0
    t.integer  "medium_count",                limit: 4,     default: 0
    t.integer  "low_count",                   limit: 4,     default: 0
    t.integer  "info_count",                  limit: 4,     default: 0
    t.string   "parameters",                  limit: 255
    t.string   "owner_type",                  limit: 255
    t.boolean  "is_upload_scan",                            default: false
    t.text     "username_param",              limit: 65535
    t.text     "password_param",              limit: 65535
    t.text     "username_param_val",          limit: 65535
    t.text     "password_param_val",          limit: 65535
    t.text     "web_login_url",               limit: 65535
  end

  add_index "scaners", ["critical_count"], name: "index_scaners_on_critical_count", using: :btree
  add_index "scaners", ["high_count"], name: "index_scaners_on_high_count", using: :btree
  add_index "scaners", ["info_count"], name: "index_scaners_on_info_count", using: :btree
  add_index "scaners", ["low_count"], name: "index_scaners_on_low_count", using: :btree
  add_index "scaners", ["medium_count"], name: "index_scaners_on_medium_count", using: :btree
  add_index "scaners", ["repo_id"], name: "index_scaners_on_repo_id", using: :btree
  add_index "scaners", ["team_id"], name: "index_scaners_on_team_id", using: :btree
  add_index "scaners", ["total_count"], name: "index_scaners_on_total_count", using: :btree
  add_index "scaners", ["user_id"], name: "index_scaners_on_user_id", using: :btree

  create_table "sessions", force: :cascade do |t|
    t.string   "session_id", limit: 255,   null: false
    t.text     "data",       limit: 65535
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "sessions", ["session_id"], name: "index_sessions_on_session_id", unique: true, using: :btree
  add_index "sessions", ["updated_at"], name: "index_sessions_on_updated_at", using: :btree

  create_table "settings", force: :cascade do |t|
    t.string   "var",        limit: 255,   null: false
    t.text     "value",      limit: 65535
    t.integer  "thing_id",   limit: 4
    t.string   "thing_type", limit: 30
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "settings", ["thing_type", "thing_id", "var"], name: "index_settings_on_thing_type_and_thing_id_and_var", unique: true, using: :btree

  create_table "taggings", force: :cascade do |t|
    t.integer  "comment_id", limit: 4
    t.integer  "tag_id",     limit: 4
    t.datetime "created_at",           null: false
    t.datetime "updated_at",           null: false
    t.integer  "finding_id", limit: 4
  end

  add_index "taggings", ["comment_id"], name: "index_taggings_on_comment_id", using: :btree
  add_index "taggings", ["tag_id"], name: "index_taggings_on_tag_id", using: :btree

  create_table "tags", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "user_id",    limit: 4
  end

  create_table "tasks", force: :cascade do |t|
    t.string   "name",              limit: 255
    t.string   "group",             limit: 255
    t.boolean  "is_active?",                    default: true
    t.datetime "created_at",                                    null: false
    t.datetime "updated_at",                                    null: false
    t.integer  "scan_type_id",      limit: 4
    t.string   "gem_name",          limit: 255
    t.boolean  "is_custom",                     default: false
    t.boolean  "can_upgrade",                   default: false
    t.boolean  "is_direct_upgrade",             default: false
  end

  create_table "tasks_users", id: false, force: :cascade do |t|
    t.integer "task_id", limit: 4, null: false
    t.integer "user_id", limit: 4, null: false
  end

  create_table "teams", force: :cascade do |t|
    t.string   "name",            limit: 255
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "total_count",     limit: 4,   default: 0
    t.integer  "critical_count",  limit: 4,   default: 0
    t.integer  "high_count",      limit: 4,   default: 0
    t.integer  "medium_count",    limit: 4,   default: 0
    t.integer  "low_count",       limit: 4,   default: 0
    t.integer  "info_count",      limit: 4,   default: 0
    t.boolean  "is_default_team",             default: false
  end

  add_index "teams", ["critical_count"], name: "index_teams_on_critical_count", using: :btree
  add_index "teams", ["high_count"], name: "index_teams_on_high_count", using: :btree
  add_index "teams", ["info_count"], name: "index_teams_on_info_count", using: :btree
  add_index "teams", ["low_count"], name: "index_teams_on_low_count", using: :btree
  add_index "teams", ["medium_count"], name: "index_teams_on_medium_count", using: :btree
  add_index "teams", ["name"], name: "index_teams_on_name", unique: true, using: :btree
  add_index "teams", ["total_count"], name: "index_teams_on_total_count", using: :btree

  create_table "teams_users", id: false, force: :cascade do |t|
    t.integer "team_id", limit: 4, null: false
    t.integer "user_id", limit: 4, null: false
  end

  create_table "uploads", force: :cascade do |t|
    t.integer  "finding_id",        limit: 4
    t.string   "file_file_name",    limit: 255
    t.string   "file_content_type", limit: 255
    t.integer  "file_file_size",    limit: 4
    t.datetime "file_updated_at"
    t.datetime "created_at",                    null: false
    t.datetime "updated_at",                    null: false
    t.integer  "user_id",           limit: 4
  end

  create_table "users", force: :cascade do |t|
    t.string   "email",                  limit: 255, default: "",    null: false
    t.string   "encrypted_password",     limit: 255, default: "",    null: false
    t.string   "reset_password_token",   limit: 255
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          limit: 4,   default: 0,     null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip",     limit: 255
    t.string   "last_sign_in_ip",        limit: 255
    t.datetime "created_at",                                         null: false
    t.datetime "updated_at",                                         null: false
    t.string   "provider",               limit: 255
    t.string   "uid",                    limit: 255
    t.string   "name",                   limit: 255
    t.string   "token",                  limit: 255
    t.boolean  "is_team_lead?",                      default: false
    t.boolean  "is_admin?",                          default: false
    t.boolean  "is_security_member?",                default: false
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree

  add_foreign_key "taggings", "comments"
  add_foreign_key "taggings", "tags"
end
