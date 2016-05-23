# Picture Voting Sample Application

## Pic Flow:
1) User texts pic to burner number
2) Webhook with pic uri
3) Application retrieves pic bytes from uri
4) Application puts pic bytes to Dropbox folder

## Vote Flow:
5) Users text pic name to burner number
6) Application increments vote count for pic name

## Report Flow:
7) Users request /report
8) Application returns JSON pic names with vote counts

## Sample JSON

Picture Message
```
{
  "type": "inboundMedia",
  "payload": "http://s3.amazonaws.com/bucket/img.png",
  "fromNumber": "+12222222222",
  "toNumber": "+13333333333"
}
```

Vote Message
```
{
  "type": "inboundText"
  ,"payload": "img.png"
  ,"fromNumber": "+12222222222"
  ,"toNumber": "+13333333333"
}
```

Sample Report
```
{[
  "pic1.jpg" : 5
  ,"pic2.jpg" : 4
  ,"picN.jpg" : 3
]}
```
