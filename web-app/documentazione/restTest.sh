rm -f out
mkfifo out
trap "rm -f out" EXIT
while true
do
  cat out | nc -l 1500 > >(
    export REQUEST=
    while read line
    do
      line=$(echo "$line" | tr -d '[\r\n]')

      if echo "$line" | grep -qE '^GET /' # se con get "GET /"
      then
        REQUEST=$(echo "$line" | cut -d ' ' -f2) # estrae la richiesta
      elif [ "x$line" = x ] # se vuota/ esce
      then
        HTTP_200="HTTP/1.1 200 OK"
        HTTP_LOCATION="Location:"
        HTTP_404="HTTP/1.1 404 Not Found"
        # call a script here
        # Note: REQUEST is exported, so the script can parse it (to answer 200/403/404 status code + content)
        if echo $REQUEST | grep -qE '^/echo/'
        then
            printf "%s\n%s %s\n\n%s\n" "$HTTP_200" "$HTTP_LOCATION" $REQUEST ${REQUEST#"/echo/"} > out
        elif echo $REQUEST | grep -qE '^/date'
        then
            date > out
        elif echo $REQUEST | grep -qE '^/stats'
        then
            vmstat -S M > out
        elif echo $REQUEST | grep -qE '^/net'
        then
            ifconfig > out
        else
            printf "%s\n%s %s\n\n%s\n" "$HTTP_404" "$HTTP_LOCATION" $REQUEST "Resource $REQUEST NOT FOUND!" > out
        fi
      fi
    done
  )
done
