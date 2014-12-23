Webservice-Monitor
==================

Simple webservice monitor with a Java &amp; MySQL back-end

Currently the application expects to find a MySQL database with the following structure:
	Tables:
		- WEBSERVICE
			Columns:
				- NAME (varchar(255))
				- URL (varchar(255))
		