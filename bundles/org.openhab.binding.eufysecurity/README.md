# EufySecurity Binding

Binding for Eufy Security Devices (https://www.eufylife.com/products/604/security)

At this time this binding only supports the Doorbell (https://www.eufylife.com/products/variant/video-doorbell/T82001J1) device.

## Supported Things

- Doorbell T8200

## Discovery

Auto-discovery of Bridge is not supported but auto discovery of devices and hubs are supported.

## Thing Configuration

The following configuration parameters are available on the Eufy Bridge:

| Parameter                | Required/Optional | Description                                                                                                                                                                    |
|--------------------------|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ID                       | Required          | The ID number of the Doorbird device. This is usually a single digit (e.g. 1).                                                                                                 |
| email Addresss           | Required          | email address used to login to smartphone app. |
| Password                 | Required          | Password of a user.  
| Region                   | Required          | Either US or EU as the server end points are different  


The following configuration parameters are available on the Doorbell thing:

| Parameter                | Required/Optional | Description                                                                                                                                                                    |
|--------------------------|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ID                       | Required          | The ID number of the doorbell device. This is usually a single digit (e.g. 1).                                                                                                 |
| Serial Number            | Required          | The device serial number                                                                                                       |
                     |
| Image Refresh Rate       | Optional          | Rate at which image channel should be automatically updated. Leave field blank (default) to disable refresh.                                                                   |
| Doorbell Off Delay       | Optional          | Number of seconds to wait before setting doorbell channel OFF after a doorbell event. Leave field blank to disable.                                                            |
| Motion Off Delay         | Optional          | Number of seconds to wait before setting motion channel OFF after a motion event. Leave field blank to disable.                                           

## Channels

The following channels are supported by the binding.

| Channel ID               | Item Type | Description                                       |
|--------------------------|-----------|---------------------------------------------------|
| doorbell                 | Trigger   | Generates PRESSED event when doorbell is pressed  |
| doorbellTimestamp        | DateTime  | Timestamp when doorbell was pressed               |
| doorbellImage            | Image     | Image captured when the doorbell was pressed      |
| motion                   | Switch    | Changes to ON when the device detects motion      |
| motionTimestamp          | DateTime  | Timestamp when motion sensor was triggered        |
| motionImage              | Image     | Image captured when motion was detected           |
| image                    | Image     | Image from the doorbell camera                    |
| imageTimestamp           | DateTime  | Time when image was captured from device          |

## Profiles

Using the system default switch profile *rawbutton-on-off-switch* in a *doorbell* channel item definition will cause ON/OFF 
states to be set when the doorbell is pressed and released.
See *Items* example below.

## Rule Actions

None at this time


## Full Example

### Things

```
Bridge eufysecurity:bridge:main "EufyBride"
    [
        emailAddress="abc@mail.com",
        userPassword="pASSwd",
        region="US"
    ]
Thing eufysecurity:t8200:doorbell Doorbird T8200
    [
        doorbirdId="1",
        DeviceSN="T82001234567N",
        imageRefreshRate=60,
        doorbellOffDelay=3,
        motionOffDelay=30
    ]
```

### Items

```
Switch                      Doorbell_Pressed
                            "Doorbell Pressed [%s]"
                            <switch>
                            ["Switch"]
                            { channel="eufysecurity:t8200:doorbell:doorbell" [profile="rawbutton-on-off-switch"] }

DateTime                    Doorbell_PressedTimestamp
                            "Doorbell Pressed Timestamp [%1$tA, %1$tm/%1$td/%1$tY %1$tl:%1$tM %1$tp]"
                            <time>
                            { channel="eufysecurity:t8200:doorbell:doorbellTimestamp" }

Image                       Doorbell_PressedImage
                            "Doorbell Pressed Image [%s]"
                            { channel="eufysecurity:t8200:doorbell:doorbellImage" }

Switch                      Doorbell_Motion
                            "Doorbell Motion [%s]"
                            <switch>
                            ["Switch"]
                            { channel="eufysecurity:t8200:doorbell:motion" }

DateTime                    Doorbell_MotionTimestamp
                            "Doorbell Motion Timestamp [%1$tA, %1$tm/%1$td/%1$tY %1$tl:%1$tM %1$tp]"
                            <time>
                            { channel="eufysecurity:t8200:doorbell:motionTimestamp" }

Image                       Doorbell_MotionDetectedImage
                            "Motion Detected Image [%s]"
                            { channel="eufysecurity:t8200:doorbell:motionImage" }

Image                       Doorbell_Image
                            "Doorbell Image [%s]"
                            { channel="eufysecurity:t8200:doorbell:image" }
```

### Sitemap

```
Frame {
    Text label="Eufy Doorbell" {
        Frame label="Image" {
            Image item=Doorbell_Image
        }
        Frame label="Events" {
            Text item=Doorbell_Pressed
            Text item=Doorbell_PressedTimestamp
            Image item=Doorbell_PressedImage
            Text item=Doorbell_Motion
            Text item=Doorbell_MotionTimestamp
            Image item=Doorbell_MotionImage
        }
    }
}
```

### Rule

```
rule "Doorbell Button Pressed"
when
    Channel "eufysecurity:t8200:doorbell:doorbell" triggered PRESSED
then
    // Do something when the doorbell is pressed
end
```