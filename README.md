# cuboid-packer
A home-grown cuboid packing engine (currently accompanied by a mediocre UI).

Hi, I'm trying to bring back to life a project bit-rotting since 2009. There is a [description](http://www.d12k.org/cubes/ "Yes, a description.") documenting the current state. It mentions three problems: the engine, a UI to interact with it, and a deployment mechanism.

Back then I thought that java webstart was a good solution to the deployment problem but there were always issues. I've tested it right now after a long time and now it doesn't work due to some security issues. Now, I'd rather drop that and let this github-place solve the deployment problem. The UI problem is still a problem because I still dislike writing UIs. Just try it and you'll notice. Maybe you can help me out.

The engine works great (as designed / within its limits) but it's the result of two weeks of unprincipled fiddling, by which I mean that it has no rigorous theoretical foundation.

# The code

The folder eclipse-projects can be used as an eclipse workspace. It contains the engine and the UI project. The latter contains some native binaries for 64bit Windows (only).

Unfortunately, the code is commented in german. I'll try to find time to translate them.