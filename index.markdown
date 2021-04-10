---
title: Test page
date: 2021-04-10 01:54:00 Z
permalink: "/"
---

<ul>
  {% for post in site.posts %}
    <li>
      <a href="{{ post.url }}">{{ post.title }}</a>
    </li>
  {% endfor %}
</ul>