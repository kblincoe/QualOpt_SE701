#!/bin/sh 
osascript <<END 
tell application "Terminal" 
	do script "cd \"`pwd`\"; ./mvnw clean test ;exit" 
	do script "cd \"`pwd`\"; yarn test ;exit" 
end tell 
END 
