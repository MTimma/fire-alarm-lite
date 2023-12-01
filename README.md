# Fire Alarm Lite

Same as <a href="https://github.com/MOsmanis/fire-alarm">Fire Alarm</a> but messages are written to a file instead of MySQL DB. 
So it could be hosted on AWS t3.micro, as MySQL DB features are not really necessary.

The file location is defined by the property `messages.file`. By default, it is located in the user folder. If changed, need to adjust the volume path in docker-compose.yml 

The apartment building I live in has very sensitive smoke detectors in apartments that are rented out to short-term tenants, which trigger a very loud alarm in the whole building. This has caused hour-long alarm sessions, including waking up from sleep because somebody left the humidifier on during the night. Usually, travelers are confused about what to do and just wait it out.

This web app informs foreigners about what to do during a fire alarm and allows them to leave a message in case it is a 100% false alarm.

Feel free to change the text and re-use it for similar situations

## License

[MIT](https://opensource.org/licenses/MIT)

Copyright (c) 2023 Martins Osmanis
