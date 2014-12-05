'use strict';

module.exports = function (grunt) {

	// This should match the beginning of <finalName> in pom.xml
	var warname = 'airlinesystem#v1##', goodwarname, badwarname;

	// Convert a version from x.x.x to 0x0x0x
	function versionConvert(v) {
		return v.split('.').reduce(function (versionStringSoFar, nextString) {
			return versionStringSoFar + ('0' + nextString).slice(-2);
		}, '');
	}

	// Figure out war name
	var version = grunt.file.readJSON('package.json').version;

	// Set options for rename task
	badwarname = warname + version + '.war';
	goodwarname = warname + versionConvert(version) + '.war';

	// Load grunt tasks automatically
	require('load-grunt-tasks')(grunt);

	// Define the configuration for all the tasks
	grunt.initConfig({

		// Generate changelog between tags
		changelog: {
			options: {}
		},

		// Update app versions
		bumpup: {
			files: ['package.json']
		},

		// Update version in pom.xml
		replace: {
			pomversion: {
				src: ['pom.xml'],
				overwrite: true,
				replacements: [{
					from: /<version grunt-target=['"]true['"]>[0-9]+\.[0-9]+\.[0-9]+<\/version>/,
					to: function () {
						var version = grunt.file.readJSON('package.json').version;
						return '<version grunt-target="true">' + version + '</version>';
					}
				}]
			}
		},

		// Rename war with correct version format
		rename: {
			war: {
				files: [{src: ['target/' + badwarname], dest: 'target/' + goodwarname}]
			}
		}
	});

	grunt.registerTask('bump', function (version) {
		var tasks = [], type;
		if (version === 'pre') {
			type = 'prerelease';
		} else if (version === 'bug') {
			type = 'patch';
		} else if (version === 'minor') {
			type = 'minor';
		} else if (version === 'major') {
			type = 'major';
		} else {
			type = version;
		}

		tasks.push('bumpup:' + type);
		tasks.push('replace:pomversion');

		grunt.task.run(tasks);
	});
};
