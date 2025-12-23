# 🔐 Security Configuration Guide

## 🚨 **IMPORTANT: Environment Variables Setup**

This project uses environment variables to keep sensitive information secure. **Never commit real passwords or API keys to GitHub!**

## 📋 **Required Environment Variables**

### **Step 1: Create .env File**
Copy the example file and add your real values:
```bash
cp .env.example .env
```

### **Step 2: Configure Your .env File**
```bash
# Database Configuration
DB_PASSWORD=your_strong_database_password
MYSQL_ROOT_PASSWORD=your_mysql_root_password

# JWT Configuration (minimum 32 characters)
JWT_SECRET=your_super_secret_jwt_key_minimum_32_characters_long

# Razorpay Configuration
RAZORPAY_KEY_ID=rzp_test_your_key_id
RAZORPAY_KEY_SECRET=your_razorpay_secret
RAZORPAY_WEBHOOK_SECRET=your_webhook_secret

# Email Configuration
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password

# Grafana Configuration
GRAFANA_ADMIN_PASSWORD=your_grafana_admin_password
```

## 🔧 **Local Development Setup**

### **For Docker Deployment:**
1. Create `.env` file in project root
2. Add all required variables
3. Run: `deploy-docker.bat` or `deploy-docker.sh`

### **For Local Development:**
1. Set environment variables in your IDE/terminal
2. Or create `.env` file and source it:
   ```bash
   # Linux/Mac
   source .env
   
   # Windows
   # Set variables manually or use .env file
   ```

## 🛡️ **Security Best Practices**

### ✅ **What We Do Right:**
- ✅ All sensitive data in environment variables
- ✅ `.env` file in `.gitignore`
- ✅ No hardcoded secrets in application code
- ✅ JWT tokens for authentication
- ✅ Password encryption with BCrypt
- ✅ CORS configuration
- ✅ Rate limiting

### ❌ **What NOT to Do:**
- ❌ Never commit `.env` file to Git
- ❌ Never hardcode passwords in code
- ❌ Never share API keys publicly
- ❌ Never use weak passwords
- ❌ Never commit real credentials

## 🔑 **How to Get API Keys**

### **Razorpay Setup:**
1. Go to https://razorpay.com/
2. Create account and get test keys
3. Add to your `.env` file

### **Gmail App Password:**
1. Enable 2-factor authentication
2. Generate app password in Google Account settings
3. Use app password (not your regular password)

## 🧪 **Testing with Environment Variables**

### **Local Testing:**
```bash
# Set environment variables
export DB_PASSWORD=test123
export JWT_SECRET=test_jwt_secret_minimum_32_characters
export RAZORPAY_KEY_ID=rzp_test_dummy
export RAZORPAY_KEY_SECRET=dummy_secret
export MAIL_USERNAME=test@example.com
export MAIL_PASSWORD=dummy_password

# Run tests
mvn test
```

### **Docker Testing:**
```bash
# Environment variables are loaded from .env file automatically
docker-compose up -d
```

## 🚀 **Production Deployment**

### **Environment Variables in Production:**
- Use your cloud provider's secret management
- AWS: Parameter Store / Secrets Manager
- Azure: Key Vault
- GCP: Secret Manager
- Kubernetes: Secrets

### **Example for AWS:**
```bash
# Store secrets in AWS Parameter Store
aws ssm put-parameter --name "/flight-booking/db-password" --value "prod_password" --type "SecureString"
aws ssm put-parameter --name "/flight-booking/jwt-secret" --value "prod_jwt_secret" --type "SecureString"
```

## 🔍 **Security Verification**

### **Check for Hardcoded Secrets:**
```bash
# Search for potential secrets
git grep -i password
git grep -i secret
git grep -i key
git grep rzp_
git grep smtp

# Should only show environment variable references like ${PASSWORD}
```

### **Verify .env is Ignored:**
```bash
# Check git status
git status

# .env should NOT appear in tracked files
```

## 🚨 **If You Accidentally Commit Secrets**

### **Immediate Actions:**
1. **Change all compromised credentials immediately**
2. **Remove from Git history:**
   ```bash
   # Remove file from Git history
   git filter-branch --force --index-filter 'git rm --cached --ignore-unmatch .env' --prune-empty --tag-name-filter cat -- --all
   
   # Force push (dangerous - only if you're sure)
   git push origin --force --all
   ```
3. **Rotate all API keys and passwords**
4. **Update environment variables**

## 📞 **Security Support**

If you find security issues:
1. **DO NOT** create public GitHub issues
2. Email security concerns privately
3. Follow responsible disclosure

## ✅ **Security Checklist**

- [ ] `.env` file created with real values
- [ ] `.env` file in `.gitignore`
- [ ] No hardcoded secrets in code
- [ ] Strong passwords used
- [ ] JWT secret is 32+ characters
- [ ] Gmail app password (not regular password)
- [ ] Razorpay test keys for development
- [ ] Production secrets in secure storage

---

**Remember: Security is everyone's responsibility! 🔐**