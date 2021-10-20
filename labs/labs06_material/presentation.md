## Compiler Extension Presentation Instructions

Background presentations will take place in week 14. We strongly
recommend that you pre-record your presentation. **[You should upload
your talk on SwitchTube](https://tube.switch.ch/channels/c1d660a4)**
(the precise channel will be linked here soon). However, if you prefer,
you can also live stream your presentation, but in that case you are
responsible if the presentation does not reach your audience due to
network quality issues.

**The presentation should be 10 minutes long.**

**Q&A session of 5-10 minutes** will follow right after the
presentation. Please make sure at least one of you is available for the
entire 20 minute slot.

**We would like each member of the group to be part of the
presentation.**

Shortly after, you will receive feedback from us regarding the content
of your presentation, as well as some general feedback on the form.

### Presentation content

Your presentation should summarize your project. In particular, we\'d
expect to see

-   a basic overview of the features you added to the compiler/language
-   some (short) programs highlighting the use of these features, with a
    description of how your extended compiler behaves on them
-   possibly some theoretical background you had to learn about to
    implement the extension
-   an overview of the changes you made to each compiler phase and/or
    which phases you added

### Presentation style

Here are some useful resources on how to prepare and give talks:

-   [How To Speak by Patrick
    Winston](https://www.youtube.com/watch?v=Unzc731iCUY)
-   [How to give a great research talk by Simon Peyton
    Jones](https://www.microsoft.com/en-us/research/academic-program/give-great-research-talk/)

Please do not use Viktor\'s videos as a model for the presentation, but
instead incorporate as many points of the talk of [Patrick
Winston](https://en.wikipedia.org/wiki/Patrick_Winston) as you believe
apply to your presentation. It is an amazing and entertaining talk,
despite (or because) it is meta-circular: he does as he says. Note:
breaking physical objects or referring to supernatural beings in your
video is not required. Use your own judgment and strike a balance in
being comfortable with what and how you are saying things and trying out
these pieces of advice.

### Instructions for video (recording or streaming)

We suggest that the speaker\'s video shows up when the speaker starts to
speak, so that the audience can relate and identify the speaker.
Afterwards, the video can be turned off and should come back on for
questions and answers. Optionally, a small video can stay on throughout
the presentation. The main content of the presentation should be a
window showing the material being presented, for example as a PDF to
which you can point to and/or annotate it. If the hardware allows you,
you can also use a tablet to simulate a blackboard presentation where
you write down everything you present, or use a combination or simple
slides and a strategy of what you will write on them.

**Video upload:** [please upload your video to this
channel](https://tube.switch.ch/channels/c1d660a4) (login with EPFL
credentials)

### Viktor\'s recording setup

For your information and not as a requirement, Viktor\'s lectures are
prepared using this hardware and software setup on Ubuntu 20 OS:

-   slides prepared using the \`beamer\` latex package
-   slides annotated using \`xournal\` PDF annotator in full screen mode
    on display size 1920x1080
-   recording using Zoom, with the following options:
    -   screen sharing PDF annotator (\`xournal\`), **without** option
        to optimize for full-screen viewing
    -   local recording, with option **Optimize for 3rd party video
        editor**
-   wacom cintiq pro display as external monitor for annotating PDF\'s
    using pen
-   video segments are cut and assembled using ffmpeg, which works very
    fast:
    -   cut like this:

```{=html}
<!-- -->
```
    fmpeg -i zoom_0.mp4 -ss 00:00:00 -to 00:02:03.00 -c copy mysegment01.mp4

      * concatenate like this:

    ffmpeg -f concat -i segmentlist.txt -c copy mycombinedvideo.mp4

where segmentlist.txt is a file containing one line per each file to
include:

    file 'mysegment01.mp4'
    file 'mysegment02.mp4'
    file 'mysegment03.mp4'

Alternatively, you can also use \`obs\` open source software. For
recording, under advanced options, you may wish to choose a 1 second key
frame interval to make cutting the video with ffmpeg work well.
