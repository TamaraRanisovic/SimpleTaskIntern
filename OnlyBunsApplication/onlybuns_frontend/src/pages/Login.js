// Importuj potrebne biblioteke
import React, { useState, useEffect  } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import { Link, useNavigate } from 'react-router-dom'; // Dodaj useNavigate
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { AppBar, Toolbar} from '@mui/material';
import logo from './photos/posticon.png';
const defaultTheme = createTheme();

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [ipAddress, setIpAddress] = useState(null); // To store the IP address
  const navigate = useNavigate();

  // Fetch the IP address from an external API
  useEffect(() => {
    fetch("https://api.ipify.org?format=json")
      .then(response => response.json())
      .then(data => {
        console.log('Fetched IP:', data.ip); // Log fetched IP
        setIpAddress(data.ip);
      })
      .catch(error => console.error('Error fetching IP:', error));
  }, []);
  

  const handleSubmit = async (event) => {
    event.preventDefault();
  
    const queryString = new URLSearchParams({
      email: email,
      password: password,
      ipAddress: ipAddress,
    }).toString();
  
    try {
      const loginResponse = await fetch(`http://localhost:8080/auth/login?${queryString}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
      });
  
      if (!loginResponse.ok) {
        const errorData = await loginResponse.json();
        setError(errorData.message || `HTTP error! Status: ${loginResponse.status}`);
        return;
      }
  
      const loginData = await loginResponse.json();
      console.log(loginData.message);
      localStorage.setItem("jwtToken", loginData.token);
  
      const decodeResponse = await fetch('http://localhost:8080/auth/decodeJwt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: loginData.token,
      });
  
      if (!decodeResponse.ok) {
        console.error('Failed to decode JWT');
        return;
      }
  
      const decodedData = await decodeResponse.json();
      const userRole = decodedData.Role;
  
      if (userRole === "REGISTERED_USER") {
        navigate('/bookTrainingUser');
      } else if (userRole === "TRAINER") {
        navigate('/bookTrainingTrainer');
      }
    } catch (error) {
      console.error("Error logging in:", error);
      setError("Login failed. Please try again.");
    }
  };
  
  
  return (
    <ThemeProvider theme={defaultTheme}>
      {/* Navigation Bar */}
      <AppBar position="static" sx={{ bgcolor: '#b4a7d6' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
            <img src={logo} alt="OnlyBuns Logo" style={{ height: '40px', marginRight: '10px' }} />
            <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
              OnlyBuns
            </Typography>
          </Box>
          </Link>
          <Box sx={{ display: 'flex', gap: 2, mr: 2 }}>
            <Button component={Link} to="/registration" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
              Sign In
            </Button>
          </Box>
        </Toolbar>
      </AppBar>
      <Container component="main" maxWidth="xs">
      <CssBaseline />
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Login
        </Typography>
        <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
          <TextField
            margin="normal"
            required
            fullWidth
            id="email"
            label="Email"
            name="email"
            autoComplete="email"
            autoFocus
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            error={Boolean(error)} // Show error style if there's an error
          />
          <TextField
            margin="normal"
            required
            fullWidth
            name="password"
            label="Password"
            type="password"
            id="password"
            autoComplete="current-password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            error={Boolean(error)} // Show error style if there's an error
          />
          {error && (
            <Typography variant="body2" color="error" align="center" sx={{ mt: 1 }}>
              {error}
            </Typography>
          )}
          <Button
            type="submit"
            sx={{
              padding: '5px 10px',
              borderRadius: '15px',
              fontSize: '1rem',
              fontWeight: 'bold',
              mt: 2,
              mb: 2,
            }}
            fullWidth
            variant="contained"
            color="secondary"
          >
            Log in
          </Button>
          <Link to="/registration"> Don't have an account? Sign up </Link>
        </Box>
      </Box>
    </Container>
    </ThemeProvider>
  );
}
