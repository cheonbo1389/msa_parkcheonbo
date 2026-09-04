0904기준해야할거
프론트 제외하고 실행 테스트
spring bus 고친거 확인
프론트랑 백엔드 연결




member


product


ordering



[ home으로 넘어오는 데이터(유저 주문 리스트) 예시]
[
    {
        "id": 1,
        "member": {
            "Id": 1,
            "name": "admin",
            "email": "admin@naver.com",
            "password": "{bcrypt}$2a$10$xdahKswqmdzT8fJfsc5tHuHykX8ZreOD9CCpEOALIe02IJnDsTBle",
            "role": "ADMIN",
            "createdTime": "2026-08-27T17:40:27.277695",
            "updatedTime": "2026-08-27T17:40:27.277695"
        },
        "product": {
            "id": 1,
            "name": "아이폰",
            "price": 2500000,
            "stockQuantity": 23,
            "member": {
                "Id": 1,
                "name": "admin",
                "email": "admin@naver.com",
                "password": "{bcrypt}$2a$10$xdahKswqmdzT8fJfsc5tHuHykX8ZreOD9CCpEOALIe02IJnDsTBle",
                "role": "ADMIN",
                "createdTime": "2026-08-27T17:40:27.277695",
                "updatedTime": "2026-08-27T17:40:27.277695"
            },
            "createdTime": "2026-09-02T15:06:34.86104",
            "hibernateLazyInitializer": {},
            "updatedTime": "2026-09-02T15:48:08.463777"
        },
        "quantity": 5,
        "orderStatus": "ORDERED",
        "createdTime": "2026-09-02T15:06:38.911123",
        "updatedTime": "2026-09-02T15:06:38.911123"
    },
    {
        "id": 2,
        "member": {
            "Id": 1,
            "name": "admin",
            "email": "admin@naver.com",
            "password": "{bcrypt}$2a$10$xdahKswqmdzT8fJfsc5tHuHykX8ZreOD9CCpEOALIe02IJnDsTBle",
            "role": "ADMIN",
            "createdTime": "2026-08-27T17:40:27.277695",
            "updatedTime": "2026-08-27T17:40:27.277695"
        },
        "product": {
            "id": 1,
            "name": "아이폰",
            "price": 2500000,
            "stockQuantity": 23,
            "member": {
                "Id": 1,
                "name": "admin",
                "email": "admin@naver.com",
                "password": "{bcrypt}$2a$10$xdahKswqmdzT8fJfsc5tHuHykX8ZreOD9CCpEOALIe02IJnDsTBle",
                "role": "ADMIN",
                "createdTime": "2026-08-27T17:40:27.277695",
                "updatedTime": "2026-08-27T17:40:27.277695"
            },
            "createdTime": "2026-09-02T15:06:34.86104",
            "hibernateLazyInitializer": {},
            "updatedTime": "2026-09-02T15:48:08.463777"
        },
        "quantity": 7,
        "orderStatus": "ORDERED",
        "createdTime": "2026-09-02T15:48:08.426223",
        "updatedTime": "2026-09-02T15:48:08.426223"
    }
]




추가 가능한 것?
css
https://react-bootstrap.netlify.app/docs/components/overlays




