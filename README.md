class-reloader
==============

Simple example of a Java ClassLoader that forcibly reloads from disk each class instance

This was inspired by the answer at [this StackOverflow answer](http://stackoverflow.com/a/3971771/7849). 
Instead of loading from `bin/` this is a complete, generalized example that tries to reload a class
using its full path, as given by the system (to be precise, the `Thread.currentThread()`) class loader.

You can try the example by editing class `reloader.A` in you favorite editor (this is a Netbeans project) 
while running the provided `Start` class as your `main`. The example will reload class `A` each second
and execute the updated constructor.
