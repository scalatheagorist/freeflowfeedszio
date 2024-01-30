curl -X POST -H "Content-Type: application/json" --data '
{
  "query": "query AllQuery($page: Int!) { all(page: $page) { result } }",
  "variables": {
    "page": 2
  }
}
' http://0.0.0.0:8989/api/graphql
