# cuboid-packer

Hi, I'm trying to bring back to life a project bit-rotting since 2009. There is a [description](http://www.d12k.org/cubes/) documenting the current state. It mentions three problems: the engine, a UI to interact with it, and a deployment mechanism.

Back then I thought that java webstart was a good solution to the deployment problem but there are always maintenance problems due to security issues. Now, I'd rather drop that and let github solve the deployment problem. The UI problem is still a problem because I still kind of dislike writing UIs. UX is not my hot topic list. Maybe you can help me out.

The engine works great (as designed / within its limits) but it's the result of two weeks of trial and error, by which I mean that it has no rigorous theoretical foundation.

# The code

The folder eclipse-projects can be used as an eclipse workspace. It contains the engine and the UI project. The latter contains some native binaries for 64bit windows (only).

Unfortunately, the code is commented in german. I'll try to find time to translate the comments to english.

To sum up, if you have worked with java and eclipse before and have a 64bit windows you should be able to get it up and running in a few minutes.
