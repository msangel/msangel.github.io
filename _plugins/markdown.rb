class Kramdown::Converter::Html
  # You can override any function from this page:
  # https://kramdown.gettalong.org/rdoc/Kramdown/Converter/Html.html
  def convert_img(el, _indent)
    if el.options.dig(:ial, :refs)&.include? 'pretty'
      el.attr['class'] = "#{el.attr['class']} col-xs-12 col-sm-10 col-sm-offset-1 col-md-8 col-md-offset-2".lstrip
      "<div class=\"row\">" \
        "<img#{html_attributes(el.attr)} />" \
        "</div>"
    else
      "<img#{html_attributes(el.attr)} />"
    end 
  end
end