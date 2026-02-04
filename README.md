# BiiMo - AI-Powered Vehicle Workshop Platform

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow.svg)](https://firebase.google.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

An intelligent Android application that revolutionizes vehicle maintenance services by connecting users with trusted workshops through AI-powered recommendations and flexible booking options.

## 📋 Overview

BiiMo addresses common pain points in traditional vehicle workshop services: unclear pricing, complicated booking processes, and difficulty in selecting appropriate services. The app leverages a custom knowledge-based AI chatbot to analyze user complaints and recommend suitable repair services.

## ✨ Key Features

- **🤖 AI Chatbot Assistant** - Custom knowledge-based NLP system for service recommendations
- **📅 Flexible Booking System** 
  - Workshop service with guaranteed time slots (no queuing)
  - Home service with technician dispatch to user location
- **💰 Real-time Cost Estimation** - Transparent pricing before booking
- **📍 Location-Based Services** - Integrated map for home service requests
- **🔍 Workshop Discovery** - Find trusted workshops with reviews and ratings
- **📊 Service History** - Track past services and maintenance schedules

## 🏗️ Architecture

```
┌─────────────────┐
│   Android App   │ (Java)
│   (Frontend)    │
└────────┬────────┘
         │
         ├──────────────┐
         │              │
┌────────▼────────┐ ┌──▼──────────────┐
│    Firebase     │ │   AI Chatbot    │
│  (Backend/DB)   │ │ (Custom NLP)    │
│                 │ │                 │
│ • Authentication│ │ • Knowledge Base│
│ • Realtime DB   │ │ • Intent Recog. │
│ • Cloud Storage │ │ • Service Match │
└─────────────────┘ └─────────────────┘
```

## 🛠️ Tech Stack

**Frontend:**
- Java (Android SDK)
- Android Studio
- Material Design Components

**Backend & Services:**
- Firebase Authentication
- Firebase Realtime Database
- Firebase Cloud Storage
- Firebase Cloud Messaging (for notifications)

**AI/ML:**
- Custom Knowledge-Based Chatbot
- Natural Language Processing
- Service Recommendation Algorithm

## 📱 Screenshots

### Dashboard, Book page and Chatbot
![Home](https://github.com/najeep24/biimo_apps/blob/745e0591043c2562e18e2075d2550f7d6b3e1200/data/book.png)

### Service Type
![type](https://github.com/najeep24/biimo_apps/blob/745e0591043c2562e18e2075d2550f7d6b3e1200/data/serviceType.png)

### Summary order
![summary](https://github.com/najeep24/biimo_apps/blob/745e0591043c2562e18e2075d2550f7d6b3e1200/data/Summary.png)

## 🚀 Getting Started

### Prerequisites

```bash
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK (API 24+)
- Firebase account
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/biimo.git
   cd biimo
   ```

2. **Setup Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Download `google-services.json` and place it in the `app/` directory
   - Enable Authentication, Realtime Database, and Cloud Storage

3. **Configure AI Chatbot**
   - Add your chatbot knowledge base to Firebase
   - Update chatbot API endpoints in `app/config/ChatbotConfig.java`

4. **Build and Run**
   - Open project in Android Studio
   - Sync Gradle files
   - Run on emulator or physical device

## 🤖 AI Chatbot Implementation

The chatbot uses a custom knowledge-based approach:

```java
// Simplified flow
UserInput → NLP Processing → Intent Recognition → 
Knowledge Base Query → Service Matching → Response Generation
```

**Key Components:**
- **Intent Classifier** - Identifies user needs from natural language
- **Knowledge Base** - Workshop services, common issues, pricing data
- **Context Manager** - Maintains conversation state
- **Recommendation Engine** - Matches problems to available services

## 📊 Project Methodology

Developed using **Agile Scrum** with 2-week sprints:
1. Requirements Gathering
2. Design & Prototyping
3. Development (Feature-based sprints)
4. Testing (White Box + UEQ)
5. Deployment
6. Review & Iteration

## 👥 Team

- **Naufal Arif** - UI/UX Designer
- **Nazhiv Ali Ash Shiddiqy** Mobile Developer & AI Integration
- **Agung Trivaldo Saputra** - Mobile Developer


