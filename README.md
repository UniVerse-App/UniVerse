# UniVerse

Student based social network centered around campus events and activities.


## This Document

This is a living document we can use to keep track of our progress, save helpful links, etc.  Don't hesitate to add things to this or make changes as you see necesary.


## Folder Structure

`uniVerse` will be the actual project folder for Android Studio

`increment_1` should contain our relevant star UML files (everyone please add your files if you have them).

`increment_2` will contain any files relevant to the SRA document.

`figma` will contain files related to design and wireframes


## Best Practices 

### General Git Stuff

For more general info on git, see this [series on the basics](https://www.atlassian.com/git)

### Git Flow

We will use the git flow methodology to maintain our code and ensure we all stay in sync.  Here is some [good documentation on the git flow methodology](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) written by Atlassian. 

It is reccomended that everyone install the git-flow package which adds a couple of helper commands that will make the gitflow methodology easier to follow.  

On ubuntu you can install with `apt-get install git-flow` or on MacOS, use homebrew `brew install git-flow`

Once the git-flow library is installed, you can reference the [git-flow docs](https://git-flow.readthedocs.io/en/latest/features.html#) for help.

The project `main` and `develop` branches have already been configured.


## Working on a Feature

Before starting or finishing a feature please make sure your develop branch is up to date.

To start a new feature run `git flow feature start my-great-feature` where `my-great-feature` is the name of the feature you are working on. This should automatically create a new branch off of `develop` with the name `feature/my-great-feature` and move you into the new branch.  You can verify this by running `git branch` and noting which branch has the `*` next to it.

A feature could be be a profile page, event card, or any self-contained piece of our app you wish to work on.  We'll want to make sure that two people aren't trying to work on the same feature. 


## Relevant Documentation

[Android Developer Reference](https://developer.android.com/)

[Firebase Reference](https://firebase.google.com/docs)

[User Authentication via Firebase Video Tutorial](https://www.youtube.com/watch?v=Z-RE1QuUWPg)

[Github Docs](https://docs.github.com/en)

[Git-Flow Library Docs](git-flow docs](https://git-flow.readthedocs.io/en/latest/features.html#)


## To Do

### Increment 1
- [x] Finalize our main class diagram
- [x] Create individual diagrams for each class
- [x] Build out our document and begin inserting diagrams in where appropriate
- [x] Submit Increment 1 - UML

### Increment 2
- [ ] Adjust UMLs where necesarry (add additional classes, refactor classes, etc.)
- [ ] Insert all updated class diagrams into the word document
- [ ] Complete requirements documents for each class
- [ ] Other stuff...
- [ ] Submit Increment 2 - SRA

