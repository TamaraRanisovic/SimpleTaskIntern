import * as React from 'react';
import { useEffect, useState, useRef } from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Box from '@mui/material/Box';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import { AppBar, Toolbar} from '@mui/material';
import { Link, useNavigate } from 'react-router-dom';
import { Dialog, DialogActions, DialogContent, DialogTitle,  List, ListItem, ListItemText, Divider } from '@mui/material';
import axios from "axios";
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { LocalizationProvider, DatePicker } from '@mui/x-date-pickers';


const defaultTheme = createTheme();

export default function BookTrainingUser() {
  const [korisnicko_ime, setKorisnickoIme] = useState('');
  const token = localStorage.getItem('jwtToken'); // Get JWT token from localStorage
  const [email, setEmail] = useState('');
  const [role, setRole] = useState('');
  const navigate = useNavigate();
  const isMounted = useRef(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMessage, setDialogMessage] = useState('');
  const navigate2 = useNavigate(); // React Router's navigate function to redirect
  const [trainingDate, setTrainingDate] = useState(null);
  const [trainings, setTrainings] = useState([]);
  const [selectedTrainingId, setSelectedTrainingId] = useState(null);
  const [bookingMessage, setBookingMessage] = useState('');

  useEffect(() => {
    if (trainingDate) {
      const formattedDate = trainingDate.toLocaleDateString('en-CA');
      axios.get(`http://localhost:8080/trainings/day?date=${formattedDate}`)
        .then((response) => setTrainings(response.data))
        .catch((error) => console.error('Error fetching trainings:', error));
    }
  }, [trainingDate]);

const handleBookingSubmit = (e) => {
  e.preventDefault();
  if (!selectedTrainingId || !korisnicko_ime) {
    setBookingMessage('Please select a training and make sure you are logged in.');
    return;
  }

  axios.post(`http://localhost:8080/trainings/book?trainingId=${selectedTrainingId}&username=${korisnicko_ime}`)
    .then(() => {
      setBookingMessage('Training booked successfully!');
    })
    .catch((error) => {
      const message = error.response?.data || 'Failed to book training.';
      setBookingMessage(`${message}`);
    });
};


const toDateObject = (dateArray) => {
  if (!Array.isArray(dateArray)) return null;
  const [year, month, day, hour, minute] = dateArray;
  return new Date(year, month - 1, day, hour, minute); // JS months are 0-based
};


  const handleCloseDialog = () => {
    setOpenDialog(false);
    navigate2('/login');
  };

  const logout = () => {
    localStorage.removeItem("jwtToken"); // Remove token

    // Redirect to login page
    window.location.href = "/login";  // or use `useNavigate` from React Router v6
  };

    
  useEffect(() => {
      if (!token) {
        setDialogMessage('No user found. Please log in.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login'); // Redirect to login after 15 seconds
        }, 15000); // Delay redirection to allow user to read the message
        return;
      }
  
      fetch('http://localhost:8080/auth/decodeJwt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: token,
      })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to decode token');
        }
        return response.json();
      })
      .then(data => {
        if (!data || data.Role !== "REGISTERED_USER") {
          setDialogMessage('Unauthorized access. Redirecting to login...');
          setOpenDialog(true);
          setTimeout(() => {
            navigate('/login');
          }, 15000);
          return;
        }
  
        // Set user data only if role is valid
        setEmail(data.Email);
        setKorisnickoIme(data.Username);
        setRole(data.Role);
      })
      .catch(error => {
        console.error('Error decoding JWT token:', error);
        setDialogMessage('Session expired or invalid token. Please log in again.');
        setOpenDialog(true);
        setTimeout(() => {
          navigate('/login');
        }, 15000);
      });
  }, [token, navigate]);



  useEffect(() => {
    isMounted.current = true;
    return () => {
      isMounted.current = false;
    };
  }, []);


  return (
    <div>
      {/* Dialog box for showing the message */}
      <Dialog open={openDialog} onClose={handleCloseDialog}>
        <DialogTitle>Notification</DialogTitle>
        <DialogContent>{dialogMessage}</DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog} color="primary">
            OK
          </Button>
        </DialogActions>
      </Dialog>
    <ThemeProvider theme={defaultTheme}>
      <AppBar position="static" sx={{ bgcolor: '#4FC3F7' }}>
        <Toolbar sx={{ justifyContent: 'space-between' }}>
          <Link to="/" style={{ textDecoration: 'none', display: 'flex', alignItems: 'center', color: 'inherit' }}>
            <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
        <Avatar sx={{ m: 1, bgcolor: '#283593' }}>
          <FitnessCenterIcon />
        </Avatar>               
        <Typography variant="h5" component="div" sx={{ fontWeight: 'bold' }}>
                FitnessApp
              </Typography>
            </Box>
          </Link>
            <Box sx={{ display: 'flex', gap: 2 }}>
              <Button component={Link} to="/bookTrainingUser" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Book training
              </Button>
              <Button component={Link} to="/cancelTrainingUser" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                Cancel training
              </Button>
              {token && korisnicko_ime ? ( 
                <Button onClick={logout} color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold'}}>
                  Logout
                </Button>
              ) : (<></>)}
            </Box>
          <Box sx={{ display: 'flex', gap: 2, mr: 2, alignItems: 'center' }}>
            {token && korisnicko_ime ? ( 
              <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                Welcome, {korisnicko_ime}
              </Typography>
            ) : (
              <>
                <Button component={Link} to="/registration" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Sign In
                </Button>
                <Button component={Link} to="/login" color="inherit" variant="outlined" sx={{ borderRadius: '20px', fontWeight: 'bold' }}>
                  Log In
                </Button>
              </>
            )}
          </Box>
        </Toolbar>
      </AppBar>
          <Container component="main" maxWidth="sm">
      <CssBaseline />
      <Box sx={{ marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <Avatar sx={{ m: 1, bgcolor: '#283593' }}>
          <FitnessCenterIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          Book a Training
        </Typography>

        <LocalizationProvider dateAdapter={AdapterDateFns}>
          <DatePicker
            label="Select a date"
            value={trainingDate}
            onChange={(newValue) => {
              setTrainingDate(newValue);
              setSelectedTrainingId(null);
              setBookingMessage('');
            }}
            renderInput={(params) => <TextField margin="normal" fullWidth {...params} />}
          />
        </LocalizationProvider>

        {trainings.length > 0 ? (
          <>
            <Typography variant="h6" sx={{ mt: 3 }}>Available Trainings</Typography>
            <List sx={{ width: '100%', bgcolor: 'background.paper' }}>
              {trainings.map((training) => (
                <React.Fragment key={training.id}>
                  <ListItem
                    button
                    selected={selectedTrainingId === training.id}
                    onClick={() => setSelectedTrainingId(training.id)}
                  >
                    <ListItemText
                      primary={`${toDateObject(training.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - ${training.trainingType}`}
                      secondary={`Duration: ${training.duration} min, Trainer: ${training.trainer}`}
                    />
                  </ListItem>
                  <Divider />
                </React.Fragment>
              ))}
            </List>

            <Button
            type="submit"
            fullWidth
            variant="contained"
            sx={{
              mt: 3,
              mb: 2,
              backgroundColor: '#4FC3F7',
              '&:hover': {
                backgroundColor: '#29B6F6', // slightly darker shade on hover
              },
            }}
            disabled={!selectedTrainingId}
            onClick={handleBookingSubmit}
          >
            Book Selected Training
          </Button>

          </>
        ) : trainingDate ? (
          <Typography sx={{ mt: 2 }}>No trainings available for selected day.</Typography>
        ) : null}

        {bookingMessage && (
          <Typography color="success.main" sx={{ mt: 2 }}>
            {bookingMessage}
          </Typography>
        )}
      </Box>
    </Container>



    </ThemeProvider>
    </div>
  );
}
