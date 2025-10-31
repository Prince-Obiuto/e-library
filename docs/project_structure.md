faculty-elibrary/
│
├── README.md
├── .gitignore
├── docker-compose.yml
│
├── backend/
│   ├── gateway-service/
│   ├── auth-service/
│   ├── user-service/
│   ├── document-service/
│   ├── approval-service/
│   ├── storage-service/
│   ├── search-service/
│   ├── notification-service/
│   └── shared-library/     # shared DTOs, utils, constants
│
├── frontend/
│   ├── elibrary-ui/        # Vaadin project
│   │   ├── src/main/java/com/faculty/elibrary/views/
│   │   │   ├── dashboard/
│   │   │   ├── documents/
│   │   │   ├── upload/
│   │   │   ├── admin/
│   │   │   ├── profile/
│   │   │   ├── search/
│   │   │   ├── login/
│   │   │   └── register/
│   │   ├── src/main/resources/
│   │   │   ├── application.properties
│   │   │   └── themes/
│   │   └── pom.xml
│   └── README.md
│
├── database/
│   ├── init.sql             # create tables & seed roles
│   ├── migrations/
│   └── backups/
│
├── docs/
│   ├── architecture-diagram.png
│   ├── wireframes/
│   ├── api-specs/
│   └── checklist.md         # progress tracker
│
└── scripts/
    ├── backup.sh
    ├── restore.sh
    ├── start.sh
    └── stop.sh
