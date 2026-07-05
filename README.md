## general
* web: https://k.co.ua/
* editor: [stackedit.io/app](https://stackedit.io/app#providerId=githubWorkspace&owner=msangel&repo=msangel.github.io&branch=master&path=_drafts%2F)
* online hyphenation: http://www.plazoo.com/ru/tools.asp

Default there defined [here](https://github.com/github/pages-gem/blob/master/lib/github-pages/configuration.rb), and it reffer to `jekyll-theme-primer`(0.5.4): https://github.com/pages-themes/primer

## running locally with the same env
Prerequirements:
* [ruby](https://rvm.io/) v3:
  * install rvm: `curl -sSL https://get.rvm.io | bash -s stable`
  * if RVM reports that PATH is not properly set up, fix current shell first: `rvm use ruby-3.0.7`
  * optional permanent RVM shell setup fix: `rvm get stable --auto-dotfiles`
  * install compatible openssl because ubuntu22.04 have only opensslv3: `rvm pkg install openssl`
  * after installing openssl, RVM suggests reinstalling all rubies; for this site reinstalling only the selected ruby is enough
  * install selected ruby: `rvm reinstall 3.0.7 --with-openssl-dir=$HOME/.rvm/usr`
  * use it as default: `rvm use 3.0.7 --default`
  * verify openssl is available: `ruby -ropenssl -e 'puts OpenSSL::OPENSSL_VERSION'`
* [Bundler](https://bundler.io/) :
  * `gem install bundler:2.2.3`
  * made available: 
    should be already there, but in case: `echo "export PATH=\"\$PATH:\$HOME/.rvm/bin\"" >> ~/.bashrc`
    * `echo "export PATH=\"\$PATH:\$HOME/.rvm/gems/default/bin\"" >> ~/.bashrc`
    * `echo "export PATH=\"\$PATH:\$HOME/.rvm/rubies/default/bin\"" >> ~/.bashrc`
## build locally
1. Install dependencies from `Gemfile.lock`: `bundle install`
2. Build static output: `bundle exec jekyll build`
3. Run locally with drafts: `bundle exec jekyll serve --drafts`
4. http://localhost:4000/ is your friend

## migration target
Move to another publishing action

## dev help
* bootstrap docs: https://getbootstrap.com/docs/3.3/css/#grid
* footer magic: https://matthewjamestaylor.com/bottom-footer
* rouge:
  * available gouge styles: `rougify help style`
  * see them online:
    * default: https://spsarolkar.github.io/rouge-theme-preview/
    * compatible with pygments: https://stylishthemes.github.io/Syntax-Themes/pygments/
  * install style: `rougify style monokai > assets/rouge.css`
* https://kramdown.gettalong.org/quickref.html

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTQ0MDc1Mjc5MCwxMTk1NDM5NDY0LDQwMT
gyMzI4NV19
-->
