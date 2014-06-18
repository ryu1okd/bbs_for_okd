# API DOCUMENT

## THREAD
 POST /api/thread  
 GET  /api/thread?title={title}

### request
  key|type|description
  :--|:--|:--
  title|TEXT|タイトル
  userId|Int|作成ユーザーID

### response sample
```
 [{
   thread:{
     "id":1,
     "title":"タイトル",
     "userId":1,
     "create_at":"2014-06-18 10:00:00"
     "update_at":"2014-06-18 14:00:00"
   }
   }]
```

## THREAD COMMENTS
 GET,POST  /api/thread/:id  
 GET       /api/thread/:id?coment={comment}&user={userName}

### request
  key|type|description
  :--|:--|:--
  id|INT|スレッドID
  comment|TEXT|コメント
  userName|TEXT|コメント

### response sample
```
 [
     {
       "id":1,
       "threadId"
       "comment":"コメント",
       "userName":"名前",
       "createAt":"2014-06-18 11:00:00"
      },
     {
       "id":2,
       "comment":"コメント",
       "userName":"名前",
       "createAt":"2014-06-18 11:00:00"
      }
   ]
```

## USER
 POST /api/user
 GET,PUT,DELETE /api/user/:id

### request
  key|type|description
  :--|:--|:--
  name|TEXT|ユーザ名

### response
```
  [{
    "id":1,
    "name":"ユーザー名"
    "createAt":"2014-06-18 12:00:00"
    }]
```
