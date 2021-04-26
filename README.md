## general
* web: https://k.co.ua/
* editor: https://manage.siteleaf.com/sites/607100b8a8712a562567c16b/pages
* online hyphenation: http://www.plazoo.com/ru/tools.asp

Default there defined [here](https://github.com/github/pages-gem/blob/master/lib/github-pages/configuration.rb), and it reffer to `jekyll-theme-primer`(0.5.4): https://github.com/pages-themes/primer

## running locally with the same env
Prerequirements:
* [ruby](https://rvm.io/)
* [Bundler](https://bundler.io/) 

Acoarding to [this instruction](https://docs.github.com/en/pages/setting-up-a-github-pages-site-with-jekyll/testing-your-github-pages-site-locally-with-jekyll): 
 1. Create `Gemfile` with this content:
  ```Gemfile
  source 'https://rubygems.org'
  gem "github-pages", "~> 214", group: :jekyll_plugins
  ```
where `214` is a a version of `github-pages` from https://pages.github.com/versions/
  
 2. run `bundle install` for installing all dependencies of `github-pages`
 3. eventually! run `bundle update github-pages` for updating dependencies
 4. run locally with: `bundle exec jekyll serve`
 5. http://localhost:4000/ is your friend

## dev help
* bootstrap docs: https://getbootstrap.com/docs/3.3/css/#grid
