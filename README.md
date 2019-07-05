# CampaignService

How to start the CampaignService application
---
1. run `chmod +x ./start.sh`
2. run `./start.sh`
3. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

Notes:
1. Didn't use rest-assured for integration testing. Instead I went with plain JerseyClient.
2. Used an in memory db.
3. Used HibernateSessionUtil because HystrixCommand runs in a new thread and it doesn't have the session.
4. Used hystrix because it is good for circuit breaking and timeouts.
5. Thought of using guava Ratelimiter but it didn't fit in this context hence created my own.
