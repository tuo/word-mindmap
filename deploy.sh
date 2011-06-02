#!/Users/twer/.rvm/rubies/ree-1.8.7-2010.02/bin/ruby

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

def vmc_update
  puts
  puts "------------------------------------------------------------------------"
  puts "updating project 'word-mindmap' hosted in cloud foundry.................."
  raise 'there is problem when updating cloud foundry project.' unless system "vmc update word-mindmap --path=target/"
  puts "updating succeed~~ yep~~~~~ :)"
end


check_vmc_existing
target_cloud_foundry
vmc_login
vmc_apps_status
package
vmc_update
