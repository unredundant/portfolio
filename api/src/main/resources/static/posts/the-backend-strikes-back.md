---
date: 2023-11-18
title: The Backend Strikes Back
description: HyperText is (almost) all you need
status: draft
---

I got my first taste of web dev in high school, on my schools robotics team... we needed a website,
and the teacher wouldn't let me embed the he-man heyyeayea song into the site, so I went and made a
second site, one where I could do whatever I damn pleased, one that capture the essence of a weird
teenage boy with a lot of time on his hands.

I wish I still had that source code, as that was by far the most fun I would have in web development for the next
decade.

Alas, this was back before this anyone ever told me about version control.

The next decade of my life was spent in the world of servers, not having to deal with the avalanche of
"progress" happening on the frontend side.

I use progress here in quotes, not to demean all the hard work and impressive feats of engineering that
have occurred in the world of JavaScript, but more to wonder if perhaps, some of us have lost the plot
a bit.

Why is it, that the last three times I've gone to create a portfolio site, my search has _begun_ with some variant
of the blursed `npx create` command. Don't I just need some HTML... a couple API endpoints and maybe a dash (dare I say
a sprinkle) of reactivity?

I had this realization while staring at an email from a founder, someone who had stumbled across my latest portfolio,
and was deeply impressed with the frontend "skills" possessed by yours truly.

Embed this https://www.youtube.com/watch?v=ToQVoyWWluQ

Dear reader, I have never felt like more of a phony. This was not something I had created, in any sense of the word.

It was merely a clone of a Tailwind template, something where I had little to no clue how the slightest bit of it
worked.

As evidence... [here](https://github.com/vercel/next.js/issues/58515) is an issue I opened with the NextJS team due to a
complete inability of mine to redirect to a new page. At the time of writing this, I'm still not sure if that is an
actual bug, or, if the server / client voodoo that NextJS does is just too opaque, and I was missing some magic function
(or worse, string tag) that would have made it all work.

BTW, as a rule, I think if you are at the point in your framework where you are asking people to use random strings as
the first line in a function to indicate whether code should run on the server or the client... you have really taken
a wrong turn. But I digress.

I'm not here to bash on NextJS... I'm here to no longer look at myself in the mirror and see a great big front-end phony.

I want to build (truly build) a portfolio site that I can be proud of, and to do it my way.  One that doesn't involve 
gigantic frameworks full of magic just to serve a user some text.  Not full of perfectly crafted CSS, tailored to soothe 
the user into maximally consumerist stupor... 

I want to build a site that is as simple as possible... one with a server, that sends some data to a client, and Reacts (heh)
when (and only when) absolutely necessary.  

So I built the damn thing in Kotlin, HTMX and pure CSS.  And I'm damn proud of the little bastard. 