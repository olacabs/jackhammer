N=0
STATUS=1
bundle config timeout 60
until [ ${N} -ge 5 ]
do
	bundle install --jobs 4 && STATUS=0 && break
	echo 'Try bundle again ...'
	N=$[${N}+1]
	sleep 1
done
exit ${STATUS}
done
