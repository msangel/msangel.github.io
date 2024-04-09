//open external links in a new window
function external_new_window() {
    for(var c = document.getElementsByTagName("a"), a = 0;a < c.length;a++) {
        var b = c[a];
        if(b.getAttribute("href") && !b.getAttribute("target") && b.hostname !== location.hostname) {
            b.target = "_blank";
            b.rel = "noopener";
        }
    }
}
//open PDF links in a new window
function pdf_new_window ()
{
    if (!document.getElementsByTagName) return false;
    var links = document.getElementsByTagName("a");
    for (var eleLink=0; eleLink < links.length; eleLink ++) {
    if ((links[eleLink].href.indexOf('.pdf') !== -1)||(links[eleLink].href.indexOf('.doc') !== -1)||(links[eleLink].href.indexOf('.docx') !== -1)) {
        links[eleLink].onclick =
        function() {
            window.open(this.href);
            return false;
        }
    }
    }
} 
pdf_new_window();
external_new_window();


document.addEventListener("DOMContentLoaded", function(){
    let xPathResult = document.evaluate(
        "//td[contains(., 'yes')]",
        document,
        null,
        XPathResult.ANY_TYPE,
        null,
    )

    let nodes = [];
    let node = xPathResult.iterateNext();
    while (node) {
        nodes.push(node);
        node = xPathResult.iterateNext();
    }

    nodes.forEach(el => {el.style.backgroundColor = '#baeda6'})

    xPathResult = document.evaluate(
        "//td[contains(., 'no')]",
        document,
        null,
        XPathResult.ANY_TYPE,
        null,
    )
    nodes = [];
    node = xPathResult.iterateNext();
    while (node) {
        nodes.push(node);
        node = xPathResult.iterateNext();
    }
    nodes.forEach(el => {el.style.backgroundColor = '#eda6a6'})
});
