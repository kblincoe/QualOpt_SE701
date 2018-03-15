#!/bin/sh 
osascript <<END 
tell application "Terminal" 
	do script "cd \"`pwd`\"; ./mvnw ;exit" 
	do script "cd \"`pwd`\"; yarn start ;exit" 
end tell 
END

