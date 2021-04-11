# general
* web: https://k.co.ua/
* editor: https://manage.siteleaf.com/sites/607100b8a8712a562567c16b/pages

Default there defined [here](https://github.com/github/pages-gem/blob/master/lib/github-pages/configuration.rb), and it reffer to `jekyll-theme-primer`: https://github.com/pages-themes/primer

# running locally with the same env:
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
