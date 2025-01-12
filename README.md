# Fire Alarm Lite

Same as <a href="https://github.com/MOsmanis/fire-alarm">Fire Alarm</a> but messages are written to a file instead of MySQL DB. 
So it could be hosted on AWS t3.micro, as MySQL DB features are not really necessary.

The file location is defined by the property `messages.file`. 

The apartment building I live in has very sensitive smoke detectors in apartments that are rented out to short-term tenants, which trigger a very loud alarm in the whole building. This has caused hour-long alarm sessions, including waking up from sleep because somebody left the humidifier on during the night. Usually, travelers are confused about what to do and just wait it out.

This web app informs foreigners about what to do during a fire alarm and allows them to leave a message in case it is a 100% false alarm.

Feel free to change the text and re-use it for similar situations

Hosted solution:

<a href="https://lacplesa24.eu/fire-alarm">lacplesa24.eu/fire-alarm</a>

<a href="https://lacplesa24.eu/messages">lacplesa24.eu/messages</a>

https://github.com/MOsmanis/fire-alarm-lite/assets/19804023/9d339b68-d314-4853-9001-b1462d8cd77a

## License

[MIT](https://opensource.org/licenses/MIT)

Copyright (c) 2023 Martins Osmanis
