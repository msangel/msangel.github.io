class Kramdown::Converter::Html
  # basic concept: https://fuzyll.com/2017/changing-markdown-output-in-jekyll/
  # will it work on github pages? no: https://github.com/gettalong/kramdown/issues/126
  # 
  # You can override any function from this page:
  # https://kramdown.gettalong.org/rdoc/Kramdown/Converter/Html.html
  # @return [String]
  # @param [Object] el
  # @param [Object] _indent
  def convert_img(el, _indent)
    el.attr['loading'] = 'lazy' if blank?(el.attr['loading'])
    if el.options.dig(:ial, :refs)&.include? 'pretty'
      el.attr['class'] = "#{el.attr['class']} col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2".lstrip
      '<div class="row">' \
        "<img#{html_attributes(el.attr)} />" \
        '</div>'
    else
      "<img#{html_attributes(el.attr)} />"
    end
  end

  def blank?(str)
    str.nil? || !str.respond_to?(:to_str) || str.include?(/[^[:space:]]/)
  end
end
