#!/Users/twer/.rvm/rubies/ree-1.8.7-2010.02/bin/ruby

#http://www.tobinharris.com/past/2010/5/10/analyse-your-git-log-with-ruby/
#http://www.jukie.net/bart/blog/pimping-out-git-log
# log --graph --pretty=format:'%C(yellow)<%an> -%C(red)%d%Creset %s %Cgreen(%cr) %Creset' --abbrev-commit --date=relative



def check_vmc_existing
  raise "'vmc' command not found. pls run 'sudo gem install vmc' or switch to right gemset." unless (system "vmc -v")
end

def target_cloud_foundry
  raise 'pls check out your internet connection.' unless system 'vmc target api.cloudfoundry.com'
end


def vmc_login
  puts "input 'clarkhtse@gmail' for email in login..."
  raise 'pls check out your internet connection.' unless system 'vmc login'
end

def vmc_apps_status
  raise 'pls check out your internet connection.' unless system 'vmc apps'
end

def package
  puts "start packaging project ,ready for update ....."
  puts "-----------------------------------------------"
  puts
  raise 'maven packaging error, check it out.' unless system 'mvn clean package'
end

def lastest_commit_number
  topest_log = "git log -1  --pretty=format:'%H'"
  result = `#{topest_log}`
end

def show_git_log_msg_for_new_commits
  last_deploy_commit_number = File.open('.last_deploy_commit_number') {|f| f.read}
  cmd = "git log --graph --pretty=format:'%C(yellow)<%an> -%C(red)%d%Creset %s %Cgreen(%cr) %Creset | %H' --abbrev-commit --date=relative"
  result = `#{cmd}`
  doc = ""
  result.each_line do |line|
    if line.include?(last_deploy_commit_number) then
      doc << "last commit goes here: " + line.split("|")[0] + " \n"
      break
    else
      doc << line.split("|")[0] + " \n"
    end
  end
  puts "Birdview on all those changeset from last commit: \n"
  puts doc
end

def vmc_update
  puts
  puts "------------------------------------------------------------------------"
  puts "updating project 'word-mindmap' hosted in cloud foundry.................."
  raise 'there is problem when updating cloud foundry project.' unless system "vmc update word-mindmap --path=target/"
  File.open(".last_deploy_commit_number", "w"){ |f| f.write lastest_commit_number}
  puts "update last commit number.....OK" 
  puts "updating succeed~~ yep~~~~~ :)"
end



check_vmc_existing
target_cloud_foundry
vmc_login
vmc_apps_status
package
show_git_log_msg_for_new_commits
vmc_update
