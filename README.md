## general
* web: https://k.co.ua/
* editor: [stackedit.io/app](https://stackedit.io/app#providerId=githubWorkspace&owner=msangel&repo=msangel.github.io&branch=master&path=_drafts%2F)
* online hyphenation: http://www.plazoo.com/ru/tools.asp

Default there defined [here](https://github.com/github/pages-gem/blob/master/lib/github-pages/configuration.rb), and it reffer to `jekyll-theme-primer`(0.5.4): https://github.com/pages-themes/primer

## running locally with the same env
Prerequirements:
* [ruby](https://rvm.io/) v3:
  * install rvm: `curl -sSL https://get.rvm.io | bash -s stable`
  * install compatible ruby as ubuntu22.04 have only opensslv3: `rvm pkg install openssl`
  * install selected ruby: `rvm install 3.0.0 --with-openssl-dir=$HOME/.rvm/usr`
  * use it as default: `rvm use 3.0.0 --default`
* [Bundler](https://bundler.io/) :
  * `gem install bundler`
  * made available: 
    should be already there, but in case: `echo "export PATH=\"\$PATH:\$HOME/.rvm/bin\"" >> ~/.bashrc`
    * `echo "export PATH=\"\$PATH:\$HOME/.rvm/gems/default/bin\"" >> ~/.bashrc`
    * `echo "export PATH=\"\$PATH:\$HOME/.rvm/rubies/default/bin\"" >> ~/.bashrc`

 1. Create `Gemfile`.
 2. run `bundle install` for installing all dependencies
 3. run locally with: `bundle exec jekyll serve --drafts`
 4. http://localhost:4000/ is your friend

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
eyJoaXN0b3J5IjpbMTE5NTQzOTQ2NCw0MDE4MjMyODVdfQ==
-->
