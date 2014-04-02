Canopy Cloud Foundry buildpack: Scala/Play
==========================================

This is a [Canopy CF buildpack](https://github.com/cloudfoundry-community/cf-docs-contrib/wiki/Buildpacks) for Scala applications.
It uses [sbt](https://github.com/sbt/sbt/) 0.13.1+.

Usage
-----

Example usage:

    $ ls
    Procfile build.sbt project src manifest.yml

		$ cat manifest.yml
		---
		# Manifest for deploying application on CloudFoundry
		applications:
		- name: AppName
			memory: 1G
			host: myapp
			buildpack: https://github.com/ahepworth/heroku-buildpack-scala.git
			instances: 1
		
    $ cf push
		Using manifest file <path>\manifest.yml

		Updating app AppName in org <org> / space development as user.name@company.net...
		OK

		Uploading AppName...
		-----> Downloaded app package (xxxxK)
		-----> Downloaded app buildpack cache (xxxM)
		Initialized empty Git repository in /tmp/buildpacks/heroku-buildpack-scala/.git/
		-----> Installing OpenJDK 1.6...
		done
		-----> Running: sbt compile stage
    ...
    -----> Scala app detected
    -----> Building app with sbt
    -----> Running: sbt clean compile stage
		...
		-----> Dropping ivy cache from the slug
		-----> Dropping compilation artifacts from the slug
		-----> Uploading droplet (116M)

		0 of 1 instances running, 1 down
		0 of 1 instances running, 1 starting
		1 of 1 instances running

		App started

		Showing health and status for app AppName in org <org> / space development as user.name@company.net...
		OK

		requested state: started
		instances: 1/1
		usage: 1G x 1 instances
		urls: myapp.apps.pivotal.io

				state     since                    cpu    memory         disk
		#0   running   2014-04-02 10:27:32 AM   0.0%   303.7M of 1G   226.2M of 1G

The buildpack will detect your app as Scala if it has the project/build.properties and either .sbt or .scala based build config. Note that #.scala files >= #.java files. It vendors a version of sbt and your populated .ivy/cache into your droplet.  The .ivy2 directory will be cached between builds to allow for faster build times.

Hacking
-------

To use this buildpack, fork it on Github.  Push up changes to your fork, then create a test app with `--buildpack <your-github-url>` and push to it.

For example, to reduce your droplet size by not including the .ivy2/cache, you could add the following.

    for DIR in $CACHED_DIRS ; do 
    rm -rf $CACHE_DIR/$DIR 
    mkdir -p $CACHE_DIR/$DIR 
    cp -r $DIR/.  $CACHE_DIR/$DIR 
    # The following 2 lines are what you would add
    echo "-----> Dropping ivy cache from the droplet" 
    rm -rf $SBT_USER_HOME/.ivy2 
    
Note: You will need to have your build copy the necessary jars to run your application to a place that will remain included with the droplet.

Commit and push the changes to your buildpack to your Github fork, then push your sample app to Canopy/CF to test.  You should see:

    ...
    -----> Dropping ivy cache from the droplet

License
-------

Licensed under the MIT License. See LICENSE file.
