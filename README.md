# Nutri Fit

A comprehensive fitness and nutrition tracking application that helps users achieve their health goals through workout planning, nutrition tracking, and social community features.

## Description

Nutri Fit is a mobile application designed to support users in their fitness journey by providing tools to track workouts, monitor nutrition, connect with friends, and visualize progress. The app features a user-friendly interface with specialized sections for exercise planning, meal tracking, social interactions, and personalized settings.

## Demo Video
- https://www.youtube.com/watch?v=p2J0F4reZrk&list=PL6BdlkdKLEB9U0F4VMXt6Ck7DX6TAdupE&index=30

## Features

### Workout Management
- Create custom workout routines with sets, reps, and weight tracking
- Browse and use pre-made workout templates for different fitness levels
- Record completed workouts and track progress over time
- View personal bests for different exercises
- Browse exercises by muscle group with a visual body map interface

### Nutrition Tracking
- Log meals with detailed macro and micronutrient information
- Search for foods using the USDA Food Data Central API
- Track daily nutrition goals with visual progress indicators
- View nutrition history and trends
- Set personalized nutrition targets based on goals

### Social Features
- Connect with friends and view their fitness activity
- Share workout achievements and progress
- Save workouts from friends as templates
- Customize profile visibility settings (public, friends-only, or private)

### User Profile
- Interactive body map showing muscle development progress
- Customizable profile with bio and fitness goals
- Track key biometric data like weight, height, and age
- Set measurement preferences (imperial or metric)

### Settings & Preferences
- Customize notification preferences
- Set privacy and security options
- Choose measurement units
- Access help and support resources

## Technical Architecture

### Frontend (Android)
- Android application built with native Java/Kotlin
- Activity-based navigation with a bottom navigation bar
- XML layouts for UI components
- External API integration with USDA Food Data Central

### Backend (Spring Boot)
- RESTful API architecture
- Controller layer for handling HTTP requests
- Service layer for business logic
- Repository layer for database interaction
- MySQL database for data persistence

### Database Schema
- User-centric data model with relationships to:
  - Workout data (templates, completed workouts)
  - Nutrition information (meals, daily targets)
  - Social connections (friends, posts)
  - Profile information (settings, biometric data)

## Team

- Michael Becker
- Nicholas Jacobs
- Anthony Nehring

## Acknowledgments

- USDA Food Data Central for nutrition data API
- Exercemus exercises repository for exercise information
