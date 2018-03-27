REM For QualOpt_SE701
REM For https://github.com/kblincoe/QualOpt_SE701/pull/95 in particular

:: This script sets up the environmant variables required for the Spring Social Framework.
:: This script must be run as an administrator.

@echo off

:: Prompt user for Twitter App Keys
set /p TWITTER_CLIENT_ID="Enter TWITTER_CLIENT_ID: "
echo %TWITTER_CLIENT_ID%

set /p TWITTER_CLIENT_SECRET="Enter TWITTER_CLIENT_SECRET: "
echo %TWITTER_CLIENT_SECRET%

echo "Setting Social Environment Variables..."

:: Set up the environment variables for Twitter App Keys for the user
set TWITTER_CLIENT_ID "%TWITTER_CLIENT_ID%" /m
set TWITTER_CLIENT_SECRET "%TWITTER_CLIENT_SECRET%" /m

pause